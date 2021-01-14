# NGINX as reverse proxy for Axon.ivy Engine
A reverse proxy (or frontend web server) hides services
and creates a single access point.

We always recommend to run a productive Axon.ivy Engine
behind a reverse proxy. As shown in this example you should
terminate the SSL traffic (HTTPS) on the reverse proxy and
let the reverse proxy communicate with the Axon.ivy Engine
over plain HTTP, because the setup is more easy and this
will also increase the performance.

Using a reverse proxy has one big advantage. You can upgrade
the reverse proxy at anytime and use the most recent security
protocols.

After starting this example you can connect to https://localhost.
You will face certificate issues in your browser, because
we can not provide trusted certificates for this example. Just
_accept the risk_ and browser the Axon.ivy Engine over the reverse
proxy.

## Security
In this example we redirect all traffic from HTTP to HTTPS if 
a HTTP request was made. Furthermore we only allow to browse
the ivy application __demo-portal__. All other traffic
is not allowed like the access to engine cockpit. Therefore
we opend a port on 8080, where you can directly access the Axon.ivy
Engine. In a productive environment you must limit the access to this
port by e.g. firewall rules.

### TLS v1.3
The configurations shows that NGINX can serve TLS v1.3 encrypted traffic over HTTPS.
A fallback for TLS v1.2 is allowed for Browsers that are not yet ready for TLS v1.3.
For demonstration purpose the HTTPS endpoint is protected by a self-signed certificate.
See `ssl_protocols TLSv1.2 TLSv1.3;` in `proxy.conf`.
Be aware that a recent distribution is required containing at least OpenSSL 1.1.1
in order to serve TLS v1.3. Thats why `nginx:stable-alpine` is used in `docker-compose.yaml`.

## HTTP2
Over HTTPS the fast revised HTTP/2 protocol is supported.
The fallback to HTTP/1.1 is allowed if the client doesn't support HTTP/2.
See `listen 443 ssl http2;` in `proxy.conf`.

## Embedded
Ivy can neatly be integrated into your existing website in an iframe.
NGINX sets a header that denies embedding ivy on any other website.
See `add_header X-Frame-Options sameorigin;` in `proxy.conf`.
Visit https://localhost/frame.html for a sample.
