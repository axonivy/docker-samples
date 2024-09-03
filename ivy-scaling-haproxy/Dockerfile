FROM axonivy/axonivy-engine:dev

ADD --chown=ivy:ivy demo-enterprise-licence.lic /usr/lib/axonivy-engine/configuration/enterprise-licence.lic
ADD --chown=ivy:ivy ivy-scaling-haproxy/ivy.yaml /usr/lib/axonivy-engine/configuration/ivy.yaml
RUN mkdir -p /usr/lib/axonivy-engine/files && chown -R ivy /usr/lib/axonivy-engine/files
