# Axon.ivy Engine integration SSO with OpenID Connect

This demo shows how to integrate the Axon.ivy Engine in a SSO enviornment which
is using Open ID Connect. Apache httpd is used as reverse proxy and authentication point
whereas Keycloak is acting as Open ID Connect Provider.

This demo follows all recommendations according setting up Apache httpd as
reverse proxy for Axon.ivy Engine. See ivy-reverse-proxy-apache demo.

## Getting started

Run `docker-compose up` and visit https://localhost. You will need to **accept
the risk** in your browser because we need to use self-signed certificats for
this demo.

You will be redirected to the Keycloak server which acts as idendity provider.
Keycloak is providing the user interface to login. You can login with these two
users:

- User: admin Password: admin
- User: demo Password: demo

After succesful login you will be redirected to the **demo-portal** application.

Challenge: Try to logout yourself ;)

## mod_auth_openidc

We use Apache httpd with the module **mod_auth_openidc**
(https://github.com/zmartzone/mod_auth_openidc) to integrate it with Keycloak.

## Idendity Provider

The administrator interface for Keycloak is here: https://localhost:8443/ You
can login with admin/admin.

## Open Issues

- Users between the idendity provider and Axon.ivy Engine are not synchronized
