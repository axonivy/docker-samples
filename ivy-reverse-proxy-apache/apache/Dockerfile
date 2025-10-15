FROM httpd:2.4

#enable already installed modules we need
RUN sed -i \
		-e 's/^#\(Include .*httpd-ssl.conf\)/\1/' \
		-e 's/^#\(LoadModule .*mod_ssl.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_socache_shmcb.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_proxy.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_proxy_http.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_rewrite.so\)/\1/' \
		conf/httpd.conf

# create empty vhost config file and include it to main config
RUN echo '\nInclude conf/vhost.conf' >> /usr/local/apache2/conf/httpd.conf && \
	touch /usr/local/apache2/conf/vhost.conf
