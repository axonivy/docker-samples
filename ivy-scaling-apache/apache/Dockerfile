FROM httpd:2.4

# enable already installed modules we need
RUN sed -i \
		-e 's/^#\(LoadModule .*mod_proxy.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_proxy_http.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_proxy_balancer.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_lbmethod_byrequests.so\)/\1/' \		
		-e 's/^#\(LoadModule .*mod_slotmem_shm.so\)/\1/' \		
		-e 's/^#\(LoadModule .*mod_status.so\)/\1/' \		
		-e 's/^#\(LoadModule .*mod_rewrite.so\)/\1/' \
		conf/httpd.conf

# create empty vhost config file and include it to main config
RUN echo '\nInclude conf/vhost.conf' >> /usr/local/apache2/conf/httpd.conf && \
	touch /usr/local/apache2/conf/vhost.conf
	