## copy Jar
cp /home/muhammed.rahman/docker-files/georep/georep-messaging-service-1.0.0.jar /opt/apps/georep/georep-messaging-service-1.0.0.jar
## copy properties file
cp /home/muhammed.rahman/docker-files/georep/application-*.properties /opt/apps/georep/config/application.properties

## Dockerfile
cp /home/muhammed.rahman/docker-files/georep/Dockerfile /opt/apps/georep/Dockerfile

docker stop georep-esb && docker rm georep-esb && docker images

docker build -t flash/georep-messaging-service:1.0.0 .

docker run --publish 9102:9102 --detach -v /var/log/esb/georep-log:/var/log/georep-log --detach -v /opt/apps/georep:/opt/apps --name georep-esb flash/georep-messaging-service:1.0.0
