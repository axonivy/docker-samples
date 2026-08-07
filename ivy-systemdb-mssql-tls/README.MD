# Axon Ivy with Microsoft SQL Server as System Database (TLS)

This example shows how to configure Axon Ivy Engine with Microsoft SQL Server as system database and encrypting the connection using TLS

## Start

```bash
docker compose up -d
```

## TLS

### Microsoft SQL Server 

The Microsoft SQL Server is configured to accept TLS connections only. See `mssql.conf`. The example uses a self-signed certificate `mssql.pem` and its private key `mssql.key`.

### Axon Ivy Engine

On Axon Ivy Engine side the self-signed certificate of the Microsoft SQL Server is stored in the `truststore.p12` file, so that Axon Ivy Engine trusts the certificate.

On the system database connection configuration (see `ivy.yaml`) the truststore is configured so that the JDBC driver is using it to establish the connection.

### Creating a self-signed certificate 

Note: in a productive environment you should use certificates issued by a certificate authority. These certificates are trusted by Axon Ivy Engine by default, so no additional configuration like truststores is necessary. 

For testing purposes it is sometimes useful to create self-signed certificates. This can be done as follows:

```bash
 openssl req -x509 -nodes -newkey rsa:2048 -subj '/CN=mssql' -keyout /tmp/mssql.key -out /tmp/mssql.pem -days 365
```



