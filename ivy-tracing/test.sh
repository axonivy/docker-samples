#!/bin/bash

proxy="${1:-http://localhost/tracing/1/pro/telemetry/17BE44A2A4E8C54D/rest.ivp}"
jaeger=http://localhost:16686

# request via proxy
status=$(curl -sL -o /dev/null -w "%{http_code}" "${proxy}")
if [[ "$status" != "200" ]]; then
  echo "FAIL: proxy returned HTTP $status, expected 200"
  exit 1
fi

# find trace
json=""
matches=0
retries=15
tags_json=$(jq -cn --arg url "$proxy" '{"http.url": $url}')
tags_encoded=$(printf '%s' "$tags_json" | jq -sRr @uri)
api="${jaeger}/api/traces?limit=2000&lookback=5m&service=Reverse%20Proxy&tags=${tags_encoded}"
for i in $(seq 1 $retries); do
  echo "$i: $api"
  json=$(curl -s "${api}")
  matches=$(echo "$json" | jq -r --arg target "$proxy" '[ .data[] ] | length')
  if [[ "$matches" -gt 0 ]]; then
    break
  fi
  sleep 1
done
if [[ "$matches" -eq 0 ]]; then
  echo "Jaeger responded: $json"
  echo "FAIL: no matching reverse-proxy trace with http.url containing '$proxy' after $retries retries"
  exit 1
fi

sleep 2 # wait until all processes sent their spans
json=$(curl -s "${api}") # re-query to get all spans of the trace

# test involved services
trace=$(echo "$json" | jq -r 'first(.data[] )')
echo "$trace" | jq -r '.traceID'
services=$(echo "$trace" | jq -r '[.processes[].serviceName] | sort | unique | join(",")')
expected="Axon Ivy Engine,Backend,Reverse Proxy"
if [[ "$services" != "$expected" ]]; then
  echo $json | jq > ../warn-jaeger-traces.json
  echo "Jaeger responded: $json"
  echo "FAIL: expected '$expected' but got '$services'"
  exit 1
fi
echo "[OK] services are '$services'"
