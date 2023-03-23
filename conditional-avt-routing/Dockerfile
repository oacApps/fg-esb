FROM adoptopenjdk/openjdk11
RUN apt-get update && apt-get install -y iputils-ping && apt-get install -y telnet && apt-get install -y less
ENV TZ=Africa/Johannesburg
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE=conditional-routing-1.0.0.jar
COPY /target/conditional-routing-1.0.0.jar /opt/apps/conditional-routing-1.0.0.jar
COPY /src/main/resources/application-qa.properties /opt/apps/config/application.properties
ENTRYPOINT ["java","-jar","/opt/apps/ConditionalRouting-1.0.0.jar","--spring.config.location=file:/opt/apps/config/application.properties"]
