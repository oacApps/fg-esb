FROM adoptopenjdk/openjdk11
RUN apt-get update && apt-get install -y iputils-ping && apt-get install -y telnet && apt-get install -y less
ENV TZ=Africa/Johannesburg
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY 1foryou-trader-1.0.4.jar /opt/apps/1foryou-trader-1.0.4.jar
COPY config/application.properties /opt/apps/config/application.properties
ENTRYPOINT ["java","-jar","/opt/apps/1foryou-trader-1.0.4.jar","--spring.config.location=file:/opt/apps/config/application.properties"]
