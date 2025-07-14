FROM bitnami/apache:2.4.64-debian-12-r0

# change to root user to allow package installations
USER 0

# install apache module auth mellon
RUN apt-get update && \ 
    apt-get install -y libapache2-mod-auth-mellon && \
	rm -rf /var/lib/apt/lists/*

# enable auth mellon module
RUN echo "\n" && echo "LoadModule auth_mellon_module /usr/lib/apache2/modules/mod_auth_mellon.so" >> /opt/bitnami/apache/conf/httpd.conf

# change back user
USER 1001
