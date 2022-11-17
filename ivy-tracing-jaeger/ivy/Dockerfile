FROM axonivy/axonivy-engine:dev

RUN mkdir /usr/lib/axonivy-engine/system/lib/agent && \
    wget -O /usr/lib/axonivy-engine/system/lib/agent/opentelemetry-javaagent-1.19.0.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/1.19.0/opentelemetry-javaagent-1.19.0.jar && \ 
    cp /usr/lib/axonivy-engine/system/plugins/ch.ivyteam.ivy.trace_*.jar /usr/lib/axonivy-engine/system/lib/agent/ch.ivyteam.ivy.trace.jar
