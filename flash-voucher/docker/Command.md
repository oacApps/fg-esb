    docker stop flash-voucher-esb && docker rm flash-voucher-esb && docker images

    docker image rm 
    cd /home/muhammed.rahman/docker-files/flash-voucher/


    ll /home/muhammed.rahman/docker-files/flash-voucher/
    
    cp /home/muhammed.rahman/docker-files/flash-voucher/1.0.0/Dockerfile /opt/apps/flash-voucher/1.0.0/Dockerfile
    cp /home/muhammed.rahman/docker-files/flash-voucher/1.0.0/application-prod.properties /opt/apps/flash-voucher/1.0.0/config/application.properties
    cp /home/muhammed.rahman/docker-files/flash-voucher/1.0.0/flash-voucher-1.0.0.jar /opt/apps/flash-voucher/1.0.0/flash-voucher-1.0.0.jar
    
    docker stop flash-voucher-esb && docker rm flash-voucher-esb && docker images
    
    

    docker build -t flash/flash-voucher-esb:1.0.0 . 

    # without Linking with DB-logging container
    docker run --publish 9016:9016 --detach -v /var/log/esb/flash-voucher:/var/log/flash-voucher --detach -v /opt/apps/flash-voucher/1.0.0:/opt/apps --name flash-voucher-esb flash/flash-voucher-esb:1.0.0
        

    tail -f /var/log/esb/flash-voucher/info.log
    less /var/log/esb/flash-voucher/info.log
    
    tail -f /var/log/esb/flash-voucher/tomcat/
    less /var/log/esb/flash-voucher/tomcat/
    
    less /var/log/esb/flash-voucher/archive/
