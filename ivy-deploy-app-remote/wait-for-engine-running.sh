#!/bin/bash

host="$1"
shift
cmd="$@"

until wget -t 1 -q http://$host:8080/ -O /dev/null; do
  echo "Wait until engine is up and running"
  sleep 1
done

echo "Engine is running - execute command"

exec $cmd