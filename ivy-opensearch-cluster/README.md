# OpenSearch Cluster
This example shows how Axon Ivy can be connected to an OpenSearch cluster.

## Why?
An OpenSearch cluster is used if a single OpenSearch instance cannot handle the
load. Or maybe a high performance OpenSearch cluster already exists in your
infrastructure.

## Troubleshooting
Maybe you face the following issue with the opensearch container: `Max
virtual memory areas vm.max_map_count [65530] likely too low, increase to at
least [262144]` In this case you must increase vm.max_map_count: `sudo sysctl -w
vm.max_map_count=262144`
