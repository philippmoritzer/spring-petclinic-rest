## Build
FROM maven:3.6.3-jdk-11-slim AS builder
COPY src /spring-petclinic-rest/src
COPY pom.xml /spring-petclinic-rest
WORKDIR /spring-petclinic-rest
RUN mvn install -DskipTests

## Package
FROM openjdk:16-jdk-alpine
COPY --from=builder /spring-petclinic-rest/target/spring-petclinic-rest-2.2.5.jar /app.jar
RUN apk update && apk add maven
COPY --from=builder /spring-petclinic-rest /spring-petclinic-rest
EXPOSE 9966
ENTRYPOINT ["java","-jar","/app.jar"]
