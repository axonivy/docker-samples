FROM axonivy/axonivy-engine:dev

RUN mkdir /ivy/system/lib/agent && \
    wget -O /ivy/system/lib/agent/opentelemetry-javaagent-1.19.0.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/1.19.0/opentelemetry-javaagent-1.19.0.jar && \ 
    cp /ivy/system/plugins/ch.ivyteam.ivy.trace_*.jar /ivy/system/lib/agent/ch.ivyteam.ivy.trace.jar
