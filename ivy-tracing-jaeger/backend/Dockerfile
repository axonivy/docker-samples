FROM tomcat:10-jdk8

RUN mkdir /usr/local/tomcat/lib/agent && \
    wget -O /usr/local/tomcat/lib/agent/opentelemetry-javaagent-1.19.0.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/1.19.0/opentelemetry-javaagent-1.19.0.jar
