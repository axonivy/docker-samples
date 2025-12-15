FROM tomcat:9-jdk8

RUN mkdir /usr/local/tomcat/lib/agent && \
    wget -O /usr/local/tomcat/lib/agent/opentelemetry-javaagent-2.22.0.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/2.22.0/opentelemetry-javaagent-2.22.0.jar
