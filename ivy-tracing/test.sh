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
api="${jaeger}/api/traces?limit=2000&lookback=5m&service=Reverse%20Proxy"
for i in {1..15}; do
  echo "$i: $api"
  json=$(curl -s "${api}")
  matches=$(echo "$json" | jq -r --arg target "$proxy" '
    [
      .data[].spans[]
      | select((.references | length) == 0)
      | select(any(.tags[]?; .key == "http.url" and (.value | contains($target))))
    ]
    | length
  ')
  if [[ "$matches" -gt 0 ]]; then
    break
  fi
  sleep 1
done
if [[ "$matches" -eq 0 ]]; then
  echo "Jaeger responded: $json"
  echo "FAIL: no matching reverse-proxy trace with http.url containing '$proxy' after 5 retries"
  exit 1
fi

# full span info
sleep 2 # wait until all processes sent their spans
json=$(curl -s "${api}")
matches=$(echo "$json" | jq -r --arg target "$proxy" '
  [
    .data[].spans[]
    | select((.references | length) == 0)
    | select(any(.tags[]?; .key == "http.url" and (.value | contains($target))))
    | .traceID
  ]
')
traceId=$(echo $matches | sed -E 's/.*"([a-z0-9]*)".*/\1/')
echo $traceId
if [[ -z "$traceId" ]]; then
  echo "Jaeger responded: $json"
  echo "FAIL: no matching trace found for '$what'"
  exit 1
fi
trace=$(echo "$json" | jq -r --arg traceId "$traceId" 'first(.data[] | select(.traceID == $traceId and (has("spanID") | not)) )')

# test involved services
services=$(echo "$trace" | jq -r '[.processes[].serviceName] | sort | unique | join(",")')
expected="Axon Ivy Engine,Backend,Reverse Proxy"
if [[ "$services" != "$expected" ]]; then
  echo $json | jq > ../warn-jaeger-traces.json
  echo "Jaeger responded: $json"
  echo "FAIL: expected '$expected' but got '$services'"
  exit 1
fi
echo "[OK] services are '$services'"
