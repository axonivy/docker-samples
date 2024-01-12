#!/usr/bin/env bash

# The oracle database image can be configured to run scripts after setup and on
# startup. However, the image contains a pre-built database which prevents
# setup scripts from being executed. As a workaround, this database setup
# script is put into the folder `/opt/oracle/scripts/startup`, which is sourced
# every time the container starts. In order to ensure that the creation of the
# necessary tablespace for the system database will only be executed the first
# time the container runs, the existence of a dummy file called "dbinit" is
# checked. If it exists, the script simply exits. Otherwise, the script will
# create that file and run to its completion.

DB_INITIALISED="/opt/oracle/oradata/dbinit"
[ -f ${DB_INITIALISED} ] && exit
touch ${DB_INITIALISED}

sqlplus / as sysdba << EOF
  CREATE TABLESPACE AxonIvy
    DATAFILE '/opt/oracle/oradata/axon_ivy.dbf' SIZE 500M
    AUTOEXTEND ON NEXT 50M
    MAXSIZE UNLIMITED;
EOF
