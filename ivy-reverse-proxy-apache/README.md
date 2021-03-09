# Apache HTTP as reverse proxy for Axon Ivy Engine
A reverse proxy (or frontend web server) hides services
and creates a single access point.

We always recommend to run a productive Axon Ivy Engine
behind a reverse proxy. As shown in this example you should
terminate the SSL traffic (HTTPS) on the reverse proxy and
let the reverse proxy communicate with the Axon Ivy Engine
over plain HTTP, because the setup is more easy and this
will also increase the performance.

Using a reverse proxy has one big advantage. You can upgrade
the reverse proxy at anytime and use the most recent security
protocols.

After starting this example you can connect to https://localhost.
You will face certificate issues in your browser, because
we can not provide trusted certificates for this example. Just
_accept the risk_ and browser the Axon Ivy Engine over the reverse
proxy.

## Security
In this example we redirect all traffic from HTTP to HTTPS if 
a HTTP request was made. Furthermore we only allow to browse
the ivy application __demo-portal__. All other traffic
is not allowed like the access to engine cockpit. Therefore
we opend a port on 8080, where you can directly access the Axon Ivy
Engine. In a productive environment you must limit the access to this
port by e.g. firewall rules.

## References
- https://httpd.apache.org/docs/2.4/howto/reverse_proxy.html
- http://httpd.apache.org/docs/2.4/mod/mod_proxy.html
