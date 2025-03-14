# Protect Axon Ivy Engine against DoS and Brute Force attacks

This example shows how you can use rate limits and iptables magic
to mitigate [DoS](https://de.wikipedia.org/wiki/Denial_of_Service)
and [brute force](https://de.wikipedia.org/wiki/Brute-Force-Methode) attacks.

The example uses [NGINX](https://nginx.org/) as a reverse proxy
configured with [rate limits](https://nginx.org/en/docs/http/ngx_http_limit_req_module.html)
so that a client can only do a certain amount of requests in a certain time period.

It uses [fail2ban](https://github.com/fail2ban/fail2ban) to analyze the
log files of [NGINX](https://nginx.org/) to detect bad behaviour and then
ban the client for a certain amount of time on network level using iptables magic.

> [fail2ban](https://github.com/fail2ban/fail2ban) only works on Linux machines.
> It does not work with Docker Desktop for Windows.

After starting this example you can connect to https://localhost.
You will face certificate issues in your browser, because we can not
provide trusted certificates for this example.
Just accept the risk and browse the Axon Ivy Engine over the reverse proxy.

On a login screen try to refresh the page. You will notice that the refresh
will take some time because of the rate limit (one request pre second).
If you hit refresh a lot of times in a short period (burst limit)
you will get `503 Service Temporary Unavailable` errors.

If you try to log in with a wrong username or password more than 5 times in 10 minutes
you get banned and you cannot reach the Engine anymore for the next 10 minutes.

## fail2ban

To analyze what fail2ban is doing have a look at the logs:

````sh
docker compose logs fail2ban
````

To see the current jail statistics:

````sh
docker compose exec -it fail2ban fail2ban-client stats
````

## NGINX rate limit

To analyze the rate limits of NIGNX have a look at message in the following format in the logs/error.log file:

````log
2025/03/11 17:16:07 [warn] 30#30: *312 delaying request, excess: 0.418, by zone "login_zone", client: 172.22.0.1, server: localhost, request: "POST /system/engine-cockpit/faces/login.xhtml HTTP/2.0", host: "localhost", referrer: "https://localhost/system/engine-cockpit/faces/login.xhtml"
````
