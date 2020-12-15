## Build
FROM maven:3.6.3-jdk-11-slim AS builder
COPY src /spring-petclinic-rest/src
COPY pom.xml /spring-petclinic-rest
WORKDIR /spring-petclinic-rest
RUN mvn install -DskipTests

## Package
FROM openjdk:11.0.1-jre-slim-stretch
COPY --from=builder /spring-petclinic-rest/target/spring-petclinic-rest-2.2.5.jar /app.jar
COPY --from=builder /spring-petclinic-rest/src /spring-petclinic-rest
EXPOSE 9966
ENTRYPOINT ["java","-jar","/app.jar"]
