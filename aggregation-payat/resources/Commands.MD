
## copy DEV properties file
cp /home/muhammed.rahman/docker-files/aggregation-payat/1.0.1/application-dev.properties /opt/apps/aggregation-payat/1.0.1/config/application.properties

## copy QA properties file
cp /home/muhammed.rahman/docker-files/aggregation-payat/1.0.1/application-qa.properties /opt/apps/aggregation-payat/1.0.1/config/application.properties

## copy PROD properties file
cp /home/muhammed.rahman/docker-files/aggregation-payat/1.0.1/application-prod.properties /opt/apps/aggregation-payat/1.0.1/config/application.properties

## copy Dockerfile
cp /home/muhammed.rahman/docker-files/aggregation-payat/1.0.1/Dockerfile /opt/apps/aggregation-payat/1.0.1/Dockerfile

## copy Jar
cp /home/muhammed.rahman/docker-files/aggregation-payat/1.0.1/aggregation-payat-1.0.1.jar /opt/apps/aggregation-payat/1.0.1/aggregation-payat-1.0.1.jar


docker stop aggregation-payat-esb && docker rm aggregation-payat-esb && docker images

docker build -t flash/aggregation-payat-esb:1.0.1 . && docker run --publish 9011:9011 --detach -v /var/log/esb/aggregation-payat:/var/log/aggregation-payat --detach -v /opt/apps/aggregation-payat/1.0.1:/opt/apps --name aggregation-payat-esb flash/aggregation-payat-esb:1.0.1
