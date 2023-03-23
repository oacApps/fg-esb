FROM maven:3.6-jdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY settings.xml /usr/src/app

ARG  REPO_TOKEN # The maven repo access token
RUN mvn -f /usr/src/app/pom.xml -s /usr/src/app/settings.xml clean package
FROM gcr.io/distroless/java
COPY --from=build /usr/src/app/target/cellular-esb-1.0.1.jar /usr/app/cellular-esb-1.0.1.jar
ENTRYPOINT ["java","-jar","/usr/app/1foryou-cellular-esb-*.jar"]

