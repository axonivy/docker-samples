#!/bin/sh 
# create a self-signed certificate to last for 10 years
openssl req -new -x509 -nodes -out server.crt -keyout server.key -days 3650
