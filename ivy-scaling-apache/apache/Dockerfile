FROM httpd:2.4
RUN echo Include conf/vhost-ivy.conf &>/usr/local/apache2/conf/httpd.conf
COPY ivy-scaling-apache/apache/ivy-loadbalancer.conf /usr/local/apache2/conf/ivy-loadbalancer.conf