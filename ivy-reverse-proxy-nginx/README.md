# ivy-nginx

This example shows how to serve ivy behind nginx as frontend webserver.

Here we use a whitelist approach, to restrict all unecessary access to ivy. Only the demo portal application is installed and will be served.

After starting this example you simply can go to http://localhost

## Embedded

Ivy can neatly be integrated into your existing website in an iframe. Nginx sets a header that denies embedding ivy on any other website. See `X-Frame-Options: sameorigin` in `ivy-reverse.conf`.

Visit http://localhost/frame.html for a sample.

## Modern procotols
NGINX is perfect as a frontend webserver before the ivy engine if modern protocols must be enforced. This approach works regardless of the protocols natively provided by the ivy engine. In this demo modern protocols are enabled on the HTTPS endpoint:

https://localhost

### TLS v1.3
The configurations shows that NGINX can serve TLS v1.3 encrypted traffic over HTTPS. A fallback for TLS v1.2 is allowed for Browsers that are not yet ready for TLS v1.3.
For demonstration purpose the HTTPS endpoint is protected by a self-signed certificate.
See `ssl_protocols TLSv1.2 TLSv1.3;`in `ivy.conf`.

Be aware that a recent distribution is required containing at least OpenSSL 1.1.1 in order to serve TLS v1.3. Thats why `nginx:stable-alpine` is used in `docker-compose.yaml`.

### HTTP2
Over HTTPS the fast revised HTTP/2 protocol is supported. The fallback to HTTP/1.1 is allowed if the client doesn't support HTTP/2.
See `listen 443 ssl http2;`in `ivy.conf`.
