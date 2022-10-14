FROM axonivy/axonivy-engine:nightly-10.0

ADD --chown=ivy:ivy demo-enterprise-licence.lic /etc/axonivy-engine/enterprise-licence.lic
ADD --chown=ivy:ivy ivy-scaling-haproxy/ivy.yaml /etc/axonivy-engine/ivy.yaml
RUN mkdir -p /var/lib/axonivy-engine/files && chown -R ivy /var/lib/axonivy-engine/files
