# Axon Ivy with Keycloak as Identity Provider

Setup of an Axon Ivy Engine with Keycloak as the Identity provider. Keycloak
will be configured as the `default` security system's identity provider.

Browse the __Engine Cockpit__, navigate to the `default` security system and,
synchronize the users.

You can log in the `demo-portal` with the following users:
- alex:alex
- louis:louis
- christian:christian

I would say `alex` is the best user.

Furthermore, you can browse the admin console of Keycloak with the user `admin`
and the password `admin` here: http://localhost:8090/


## JWT Token lease

For authenticated REST requests against the ivy backend you can lease a valid token as follows.

```
curl -L -X POST 'http://localhost:8090/realms/ivytestrealm/protocol/openid-connect/token' \
-H 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=ivy-test-client' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_secret=tqCErJA84EJKdbD8Y3Bz8mZBMW86Hmpw' \
--data-urlencode 'scope=openid' \
--data-urlencode 'username=alex' \
--data-urlencode 'password=alex'
```
