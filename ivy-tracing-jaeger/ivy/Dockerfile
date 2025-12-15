FROM axonivy/axonivy-engine:nightly-10.0

RUN mkdir system/lib/agent && \
    wget -O system/lib/agent/opentelemetry-javaagent-2.22.0.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/2.22.0/opentelemetry-javaagent-2.22.0.jar && \ 
    cp system/plugins/ch.ivyteam.ivy.trace_*.jar system/lib/agent/ch.ivyteam.ivy.trace.jar
