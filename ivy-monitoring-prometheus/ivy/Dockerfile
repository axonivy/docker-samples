FROM axonivy/axonivy-engine:dev

ADD --chown=ivy deploy/tracing.iar /ivy/deploy/tracing.iar 

RUN mkdir /ivy/system/lib/agent && \
    wget -O /ivy/system/lib/agent/opentelemetry-javaagent-2.9.0.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/2.9.0/opentelemetry-javaagent-2.9.0.jar && \ 
    cp /ivy/system/plugins/ch.ivyteam.ivy.trace_*.jar /ivy/system/lib/agent/ch.ivyteam.ivy.trace.jar
