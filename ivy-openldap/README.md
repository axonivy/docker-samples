# Axon Ivy with OpenLdap as Identity Provider

Setup of an Axon Ivy Engine with OpenLdap as identity provider.
OpenLdap will be configured as identity provider of the `default`
security system.

Browse the __Engine Cockpit__, navigate to the `default` security system
and synchronize the users.

You can browse the LDAP tree with the phpldapadmin tool: http://localhost:8090/

* User: `cn=admin,dc=example,dc=org`
* Password: `admin`
