FROM axonivy/axonivy-engine:dev

ADD --chown=ivy:ivy demo-enterprise-licence.lic /etc/axonivy-engine/enterprise-licence.lic
ADD --chown=ivy:ivy ivy-scaling-apache/ivy/ivy.yaml /etc/axonivy-engine/ivy.yaml
RUN mkdir -p /var/lib/axonivy-engine/files && chown -R ivy /var/lib/axonivy-engine/files
