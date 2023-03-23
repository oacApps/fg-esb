# Step 1: building jar file
FROM maven:3.5-jdk-11 AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests

# Step 2: Creating docker image
FROM openjdk:11-slim-buster
RUN apt-get update && apt-get install -y iputils-ping && apt-get install -y telnet && apt-get install -y less
ENV TZ=Africa/Johannesburg
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir /opt/apps
COPY --from=build /project/target/credential-management-service-0.0.1-SNAPSHOT.jar /opt/apps/credential-management-service.jar
COPY --from=build /project/resources/application-local.yml /opt/apps/config/application-local.yml

#WORKDIR /opt/apps
#CMD "java" "-jar" "credential-management-service.jar" "--spring.config.location=file:/opt/apps/config/application-local.yml"
ENTRYPOINT ["java","-jar","/opt/apps/credential-management-service.jar","--spring.config.location=file:/opt/apps/config/application-local.yml"]