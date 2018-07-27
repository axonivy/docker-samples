# docker-samples

This repository shows you different docker-compose setups with ivy.

## ivy

Running ivy in demo mode. Runs without license.

## ivy-mariadb

Setup of an ivy engine with maria db as system database.

Environments variables ared used to setup the connection between
ivy and mariadb.

## ivy-mssql

Setup of an ivy engine with microsoft sql server as system database.

Environments variables ared used to setup the connection between
ivy and mssql.

## ivy-mysql-elasticsearch

Setup of an ivy engine with mysql as system database and
elasticsearch as external service.

Environments variables ared used to setup the connection between
ivy, mysql and elasticsearch.

Docker secrets are used to configure passwords.

Maybe you face the following issue with the elasticsearch container: `Max virtual memory areas vm.max_map_count [65530] likely too low, increase to at least [262144]` In this case you must increase vm.max_map_count: `sudo sysctl -w vm.max_map_count=262144`

## ivy-postgres

Setup of an ivy engine with postgres as system database.
`ivy.yaml` is used to setup the connection between
ivy and postgres.

### Remarks

#### Licence

All these samples are shipped with a demo-licence. You can 
run the Portal application with one user at a time.
