FROM adoptopenjdk/openjdk11
RUN apt-get update && apt-get install -y iputils-ping telnet less net-tools
ENV TZ=Africa/Johannesburg
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY cellular-esb-1.0.1.jar /opt/apps/cellular-esb-1.0.1.jar
COPY config/application.properties /opt/apps/config/application.properties
ENTRYPOINT ["java","-jar","/opt/apps/cellular-esb-1.0.1.jar","--spring.config.location=file:/opt/apps/config/application.properties"]
