FROM debian:bookworm-slim

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl gnupg2 ca-certificates lsb-release debian-archive-keyring \
    && curl https://nginx.org/keys/nginx_signing.key | gpg --dearmor -o /usr/share/keyrings/nginx-archive-keyring.gpg \
    && echo "deb [signed-by=/usr/share/keyrings/nginx-archive-keyring.gpg] https://nginx.org/packages/debian $(. /etc/os-release && echo ${VERSION_CODENAME}) nginx" > /etc/apt/sources.list.d/nginx.list \
    && printf "Package: *\nPin: origin nginx.org\nPin: release o=nginx\nPin-Priority: 900\n" > /etc/apt/preferences.d/99nginx \
    && apt-get update \
    && apt-get install -y --no-install-recommends nginx nginx-module-otel \
    && rm -rf /var/lib/apt/lists/*

EXPOSE 80 443

STOPSIGNAL SIGQUIT

CMD ["nginx", "-g", "daemon off;"]
