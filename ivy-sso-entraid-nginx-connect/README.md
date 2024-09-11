# Single Sign-On (SSO) with EntraID and Nginx for Axon Ivy Engine
This demonstrates how to configure Single Sign-On (SSO) using EntraID (Azure AD) and Nginx for the Axon Ivy Engine.

**Notes**: 
- OpenResty image will be used in this example instead of a standard Nginx one, since it extends Nginx with additional features and modules (`lua-nginx-module` and `ngx_http_lua_module`) that are particularly useful for handling Lua scripts, which are essential for implementing OpenID Connect (OIDC) authentication.
- It uses the library `lua-resty-openidc` as the implementation of OpenID Connect in Nginx, with its dependencies: `lua-resty-http`, `lua-resty-session` and `lua-resty-jwt`. See [more](https://github.com/zmartzone/lua-resty-openidc). 

## Prerequisites
- Docker and Docker Compose installed on your system.
- Access to Azure AD to configure the application and obtain client ID, tenant ID, and client secret.
- SSL certificate (server.crt and server.key) for securing Nginx.

## Configuration Steps
### 1. Create Microsoft Entra ID App
Follow this instruction to [Create Microsoft Entra ID App](https://developer.axonivy.com/doc/11.3/engine-guide/integration/identity-provider/microsoft-entra-id/microsoft-entra-id-app.html#microsoft-entra-id-app)  
We will need to:
- Register an application
- Grant application permissions
- Create a client secret

Finally, modify the nginx.conf with your client_secret, client_id (application ID) and tenant ID.

### 2. Replace SSL Certificate
Replace your own certificate in nginx\certs or create a self-sign certificate.

### 3. Build and start up docker compose
Run `docker compose up` to build the nginx image and start up all services.

### 4. Add user for testing
Go to the engine cockpit and add a new user in your testing security system, be noticed that the user's `name` should match the [ID Token](https://learn.microsoft.com/en-us/entra/identity-platform/id-token-claims-reference) claim configured in the nginx.conf which is forwarded in the header, in this sample it's `id_token.email`, so you need to use the user's email from the Azure system.

Now open your testing app, you will be redirected to Azure AD for authentication.
Upon successful login, you will be redirected to the Axon Ivy Engine with the user email passed in the X-Forwarded-User header.