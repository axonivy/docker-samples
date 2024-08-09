# Use the official OpenResty Docker image as the base
FROM openresty/openresty:focal

# install dependencies
RUN ["luarocks", "install", "lua-resty-session", "3.10"]
RUN ["luarocks", "install", "lua-resty-http"]
RUN ["luarocks", "install", "lua-resty-jwt"]

# Install OpenID Connect Lua library
RUN ["luarocks", "install", "lua-resty-openidc"]

# Copy certificates to ssl folder
COPY certs/server.crt /etc/nginx/ssl/server.crt
COPY certs/server.key /etc/nginx/ssl/server.key

# Copy your NGINX configuration file
COPY nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

# Expose port
EXPOSE 80
EXPOSE 443

# Start OpenResty
CMD ["/usr/local/openresty/bin/openresty", "-g", "daemon off;"]