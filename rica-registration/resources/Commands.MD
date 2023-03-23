
## copy DEV properties file
cp /home/muhammed.rahman/docker-files/rica/1.0.0/application-dev.properties /opt/apps/rica/1.0.0/config/application.properties

## copy QA properties file
cp /home/muhammed.rahman/docker-files/rica/1.0.0/application-qa.properties /opt/apps/ricav/1.0.0/config/application.properties

## copy PROD properties file
cp /home/muhammed.rahman/docker-files/rica/1.0.0/application-prod.properties /opt/apps/rica/1.0.0/config/application.properties

## copy Dockerfile
cp /home/muhammed.rahman/docker-files/rica/1.0.0/Dockerfile /opt/apps/rica/1.0.0/Dockerfile

## copy Jar
cp /home/muhammed.rahman/docker-files/rica/1.0.0/aggregation-dstv-1.0.0.jar /opt/apps/rica/1.0.0/aggregation-dstv-1.0.0.jar


docker stop rica-esb && docker rm rica-esb && docker images

docker build -t flash/rica-esb:1.0.1 .
docker run --publish 9014:9014 --detach -v /var/log/esb/rica:/var/log/rica --detach -v /opt/apps/rica/1.0.0:/opt/apps --name rica-esb flash/rica-esb:1.0.0
