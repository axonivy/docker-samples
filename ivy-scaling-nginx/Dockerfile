FROM axonivy/axonivy-engine:nightly-12.0

ADD --chown=ivy:ivy demo-enterprise-licence.lic /ivy/configuration/enterprise-licence.lic
ADD --chown=ivy:ivy ivy-scaling-nginx/ivy.yaml /ivy/configuration/ivy.yaml

ADD --chown=ivy:ivy ivy-scaling-nginx/excel-connector.zip /ivy/deploy/excel-connector.zip
ADD --chown=ivy:ivy ivy-scaling-nginx/excel-demo.zip /ivy/deploy/excel-demo.zip