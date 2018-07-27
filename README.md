# docker-samples

This repository shows you different docker-compose setups with ivy.

## ivy

Running ivy in demo mode. Runs without license.

## ivy-db-mariadb
## ivy-db-mssql
## ivy-db-mysql
## ivy-db-postgres

Setup of an ivy engine with a particular database.

## ivy-elasticsearch

Setup of an ivy engine with an external elasticsearch server.

Maybe you face the following issue with the elasticsearch container: `Max virtual memory areas vm.max_map_count [65530] likely too low, increase to at least [262144]` In this case you must increase vm.max_map_count: `sudo sysctl -w vm.max_map_count=262144`

## ivy-enviornemnt-variables

Setup of an ivy engine which is conifugred with enviornment variables.

## ivy-secrets

Setup of an ivy engine which uses docker secrets for passwords.

### Remarks

#### Licence

All these samples are shipped with a demo-licence. You can 
run the Portal application with one user at a time.
