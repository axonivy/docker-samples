FROM bitnami/apache:2.4.65-debian-12-r0

# change to root user to allow package installations
USER 0

# install apache module auth openidc
RUN apt-get update && \ 
    apt-get install -y libapache2-mod-auth-openidc && \
	rm -rf /var/lib/apt/lists/*

# enable auth openidc module
RUN echo "\n" && echo "LoadModule auth_openidc_module /usr/lib/apache2/modules/mod_auth_openidc.so" >> /opt/bitnami/apache/conf/httpd.conf


# change back user
USER 1001
