FROM ubuntu:18.04

#update package manager and do some environment settings
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Europe/Berlin
RUN apt-get update

# install apache
RUN apt-get install -y apache2 

# forward apache request and error logs to docker log collector
RUN ln -sf /dev/stdout /var/log/apache2/access.log \
	&& ln -sf /dev/stderr /var/log/apache2/error.log
	
#install module for PHP (just used for debugging)
RUN apt-get install -y libapache2-mod-php7.2

#install mod-auth-mellon for SAML
RUN apt-get install -y libapache2-mod-auth-mellon

# install mod-auth-openidc for OAuth/OpenID Connect
RUN apt-get install -y libapache2-mod-auth-openidc

#enable apache modules
RUN a2enmod proxy_http \
    && a2enmod ssl \
	&& a2enmod php7.2 \
	&& a2enmod auth_mellon \
	&& a2enmod headers

#copy vhosts and enable ssl vhost
RUN a2ensite default-ssl
COPY 000-default.conf /etc/apache2/sites-available/000-default.conf
COPY default-ssl.conf /etc/apache2/sites-available/default-ssl.conf
COPY default-site-html /var/www/html

#add script for creation of SAML SP metadata XML
COPY saml-config /etc/apache2/saml-config

#run apache in foreground
CMD /usr/sbin/apache2ctl -D FOREGROUND