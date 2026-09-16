#!/bin/bash

proxy="${1:-http://localhost/tracing/1/pro/telemetry/17BE44A2A4E8C54D/rest.ivp}"
jaeger=http://localhost:16686
test_start=$(date -u +%Y-%m-%dT%H:%M:%SZ) # lower bound of this run; api_v3 requires explicit bounds, v1 used lookback=5m

# request via proxy (the reverse proxy may need a moment until it accepts requests)
retries=15
status=""
for i in $(seq 1 $retries); do
  status=$(curl -sL -o /dev/null -w "%{http_code}" "${proxy}")
  [[ "$status" == "200" ]] && break
  sleep 1
done
if [[ "$status" != "200" ]]; then
  echo "FAIL: proxy returned HTTP $status, expected 200 after $retries retries"
  exit 1
fi

# find trace via api_v3 (the v1 /api/traces search was removed in Jaeger 2.21, PR #9260).
# api_v3 exists since long, trace-summaries since 2.19, so this also works on 2.20.
json=""
matches=0
retries=15
tags_json=$(jq -cn --arg url "$proxy" '{"http.url": $url}')
tags_encoded=$(printf '%s' "$tags_json" | jq -sRr @uri)
for i in $(seq 1 $retries); do
  end_max=$(date -u +%Y-%m-%dT%H:%M:%SZ)
  api="${jaeger}/api/v3/trace-summaries?query.serviceName=Reverse%20Proxy&query.searchDepth=2000&query.startTimeMin=${test_start}&query.startTimeMax=${end_max}&query.attributes=${tags_encoded}"
  echo "$i: $api"
  json=$(curl -s "${api}")
  matches=$(echo "$json" | jq -r '(.summaries // []) | length' 2>/dev/null || echo 0)
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
end_max=$(date -u +%Y-%m-%dT%H:%M:%SZ)
api="${jaeger}/api/v3/trace-summaries?query.serviceName=Reverse%20Proxy&query.searchDepth=2000&query.startTimeMin=${test_start}&query.startTimeMax=${end_max}&query.attributes=${tags_encoded}"
json=$(curl -s "${api}") # re-query to get all spans of the trace

# test involved services
trace=$(echo "$json" | jq -r '.summaries[0]')
echo "$trace" | jq -r '.traceId'
services=$(echo "$trace" | jq -r '[.services[].name] | sort | unique | join(",")')
expected="Axon Ivy Engine,Backend,Reverse Proxy"
if [[ "$services" != "$expected" ]]; then
  echo $json | jq > ../warn-jaeger-traces.json
  echo "Jaeger responded: $json"
  echo "FAIL: expected '$expected' but got '$services'"
  exit 1
fi
echo "[OK] services are '$services'"
