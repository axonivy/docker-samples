# ivy-sso-saml-apache-keycloak


This repository shows you how to setup SAML 2.0 including all needed components:
- Axon.ivy Server acting as application server
- Apache httpd acting as web application firewall and authentication endpoint
- Keycloak acting as SAML 2.0 ID Provider

![Infrastructure Overview](infrastructure.png)

## How to use

After running the docker composition, visit http://localhost


## Currently not provided / not solved

- Improve documentation
- Solve issues with self-signed HTTPS certs
- sync users beween Ivy and Keycloak with LDAP?


