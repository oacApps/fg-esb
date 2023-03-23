
## copy DEV properties file
cp /home/muhammed.rahman/docker-files/aggregation-dstv/1.0.1/application-dev.properties /opt/apps/aggregation-dstv/1.0.1/config/application.properties

## copy QA properties file
cp /home/muhammed.rahman/docker-files/aggregation-dstv/1.0.1/application-qa.properties /opt/apps/aggregation-dstv/1.0.1/config/application.properties

## copy PROD properties file
cp /home/muhammed.rahman/docker-files/aggregation-dstv/1.0.1/application-prod.properties /opt/apps/aggregation-dstv/1.0.1/config/application.properties

## copy Dockerfile
cp /home/muhammed.rahman/docker-files/aggregation-dstv/1.0.1/Dockerfile /opt/apps/aggregation-dstv/1.0.1/Dockerfile

## copy Jar
cp /home/muhammed.rahman/docker-files/aggregation-dstv/1.0.1/aggregation-dstv-1.0.1.jar /opt/apps/aggregation-dstv/1.0.1/aggregation-dstv-1.0.1.jar


docker stop aggregation-dstv-esb && docker rm aggregation-dstv-esb && docker images
docker ps
docker build -t flash/aggregation-dstv-esb:1.0.1 . && 
docker run --publish 9012:9012 --detach -v /var/log/esb/aggregation-dstv:/var/log/aggregation-dstv --detach -v /opt/apps/aggregation-dstv/1.0.1:/opt/apps --name aggregation-dstv-esb flash/aggregation-dstv-esb:1.0.1
