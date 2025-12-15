FROM axonivy/axonivy-engine:nightly-12.0

RUN mkdir /ivy/system/lib/agent && \
    wget -O /ivy/system/lib/agent/opentelemetry-javaagent.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/2.22.0/opentelemetry-javaagent-2.22.0.jar && \ 
    cp /ivy/system/plugins/ch.ivyteam.ivy.trace_*.jar /ivy/system/lib/agent/ch.ivyteam.ivy.trace.jar
