# ivy-nginx

This example shows how to serve ivy behind nginx as frontend webserver.

Here we use a whitelist approach, to restrict all unecessary access to ivy. Only the demo portal application is installed and will be served.

After starting this example you simply can go to http://localhost

## Embedded

Ivy can neatly be integrated into your existing website in an iframe. Nginx sets a header that denies embedding ivy on any other website. See `X-Frame-Options: sameorigin` in `ivy-reverse.conf`.

Visit http://localhost/frame.html for a sample.