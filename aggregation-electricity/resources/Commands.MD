
## copy DEV properties file
cp /home/muhammed.rahman/docker-files/aggregation-electricity/1.0.1/application-dev.properties /opt/apps/aggregation-electricity/1.0.1/config/application.properties

## copy QA properties file
cp /home/muhammed.rahman/docker-files/aggregation-electricity/1.0.1/application-qa.properties /opt/apps/aggregation-electricity/1.0.1/config/application.properties

## copy PROD properties file
cp /home/muhammed.rahman/docker-files/aggregation-electricity/1.0.1/application-prod.properties /opt/apps/aggregation-electricity/1.0.1/config/application.properties

## copy Dockerfile
cp /home/muhammed.rahman/docker-files/aggregation-electricity/1.0.1/Dockerfile /opt/apps/aggregation-electricity/1.0.1/Dockerfile

## copy Jar
cp /home/muhammed.rahman/docker-files/aggregation-electricity/1.0.1/aggregation-electricity-1.0.1.jar /opt/apps/aggregation-electricity/1.0.1/aggregation-electricity-1.0.1.jar && ll


docker stop aggregation-electricity-esb && docker rm aggregation-electricity-esb && docker images

docker build -t flash/aggregation-electricity-esb:1.0.1 . && docker run --publish 9013:9013 --detach -v /var/log/esb/aggregation-electricity:/var/log/aggregation-electricity --detach -v /opt/apps/aggregation-electricity/1.0.1:/opt/apps --name aggregation-electricity-esb flash/aggregation-electricity-esb:1.0.1 && docker ps
