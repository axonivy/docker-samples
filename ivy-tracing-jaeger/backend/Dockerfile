FROM maven:3.9.10-eclipse-temurin-21 AS build-rest-service
COPY rest-service /usr/src/rest-service
WORKDIR /usr/src/rest-service
RUN mvn clean package

FROM maven:3.9.10-eclipse-temurin-21 AS build-soap-service
COPY soap-service /usr/src/soap-service
WORKDIR /usr/src/soap-service
RUN mvn clean package

FROM tomcat:11-jdk21

RUN mkdir /usr/local/tomcat/lib/agent && \
    wget -O /usr/local/tomcat/lib/agent/opentelemetry-javaagent-2.9.0.jar -q -N https://search.maven.org/remotecontent?filepath=io/opentelemetry/javaagent/opentelemetry-javaagent/2.9.0/opentelemetry-javaagent-2.9.0.jar
COPY --from=build-rest-service /usr/src/rest-service/target/rest-service.war /usr/local/tomcat/webapps/rest-service.war
COPY --from=build-soap-service /usr/src/soap-service/target/soap-service.war /usr/local/tomcat/webapps/soap-service.war