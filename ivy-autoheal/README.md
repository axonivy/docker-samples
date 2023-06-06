# Auto-Heal

Axon Ivy Engine implements with Axon Ivy 11.1 a `HEALTHCHECK`.
With an the additional container `willfarrell/autoheal` will check
the health state of other containers and will restart them if they are
unhealth. 

https://hub.docker.com/r/willfarrell/autoheal

For testing purpose we have redefined the health check in `compose.yaml`
for the Axon Ivy Engine, so you can change the params and test what 
happs if you make a health check which will always fail e.g. change
the hostname from `localhost` to `unknown`.

