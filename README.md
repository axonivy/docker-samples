# docker-samples

This repository shows you different docker-compose setups with ivy.

    Attention: You need to copy a valid license file in ivy/configuration
    with the hostname ivy.

## ivy

Running ivy in demo mode. Runs without license.

## ivy-mariadb

Setup of an ivy engine with maria db as system database.

Environments variables ared used to setup the connection between
ivy and mariadb.

## ivy-mysql-elasticsearch

Setup of an ivy engine with mysql as system database and
elasticsearch as external service.

Environments variables ared used to setup the connection between
ivy, mysql and elasticsearch.

Docker secrets are used to configure passwords.

## ivy-postgres

Setup of an ivy engine with postgres as system database.
`ivy.yaml` is used to setup the connection between
ivy and postgres.
