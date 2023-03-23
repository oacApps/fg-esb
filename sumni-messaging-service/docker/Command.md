## copy Jar
cp sumni-messaging-service-1.0.0.jar /opt/apps/sumni/sumni-messaging-service-1.0.0.jar
## copy properties file
cp application-*.properties /opt/apps/sumni/config/application.properties

## Dockerfile
cp /home/muhammed.rahman/docker-files/sumni/Dockerfile /opt/apps/sumni/Dockerfile

docker stop sumni-esb && docker rm sumni-esb && docker images

docker build -t flash/sumni-messaging-service:1.0.0 .

docker run --publish 9101:9101 --detach -v /var/log/esb/sumni-log:/var/log/sumni-log --detach -v /opt/apps/sumni:/opt/apps --name sumni-esb flash/sumni-messaging-service:1.0.0
