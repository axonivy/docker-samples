FROM httpd:2.4

# install apache module auth mellon
RUN apt-get update && \ 
    apt-get install -y libapache2-mod-auth-mellon && \
	rm -rf /var/lib/apt/lists/*

# enable already installed modules we need
RUN sed -i \
		-e 's/^#\(Include .*httpd-ssl.conf\)/\1/' \
		-e 's/^#\(LoadModule .*mod_ssl.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_socache_shmcb.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_proxy.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_proxy_http.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_rewrite.so\)/\1/' \
		conf/httpd.conf

# enable auth openidc module
# create empty vhost config file and include it to main config
RUN echo '\nLoadModule auth_mellon_module /usr/lib/apache2/modules/mod_auth_mellon.so' >> /usr/local/apache2/conf/httpd.conf && \
    echo '\nInclude conf/vhost.conf' >> /usr/local/apache2/conf/httpd.conf && \
	touch /usr/local/apache2/conf/vhost.conf
