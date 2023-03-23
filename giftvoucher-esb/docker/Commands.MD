

    docker stop giftvoucher-esb && docker rm giftvoucher-esb && docker images

    docker image rm 
    cd /home/muhammed.rahman/docker-files/giftvoucher/


    cp /home/muhammed.rahman/docker-files/giftvoucher/1.0.1/Dockerfile /opt/apps/giftvoucher/1.0.1/Dockerfile
    
    cp /home/muhammed.rahman/docker-files/giftvoucher/1.0.1/application-qa.properties /opt/apps/giftvoucher/1.0.1/config/application.properties
    
    cp /home/muhammed.rahman/docker-files/giftvoucher/1.0.1/application-prod.properties /opt/apps/giftvoucher/1.0.1/config/application.properties
    
    cp /home/muhammed.rahman/docker-files/giftvoucher/1.0.1/GetVoucherInfo.json /opt/apps/giftvoucher/1.0.1/dataFiles/GetVoucherInfo.json
    
    cp /home/muhammed.rahman/docker-files/giftvoucher/1.0.2/giftvoucher-esb-1.0.2.jar /opt/apps/giftvoucher/1.0.2/giftvoucher-esb-1.0.2.jar
    
    docker stop giftvoucher-esb && docker rm giftvoucher-esb && docker ps
    
    /opt/apps/giftvoucher/
    /opt/apps/giftvoucher/dataFiles

    docker build -t flash/giftvoucher-esb:1.0.2 . 

    # without Linking with DB-logging container
    docker run --publish 9003:9003 --detach -v /var/log/esb/giftVoucher:/var/log/giftVoucher --detach -v /opt/apps/giftvoucher/1.0.2:/opt/apps --name giftvoucher-esb flash/giftvoucher-esb:1.0.2

    tail -f /var/log/esb/giftVoucher-log/giftVoucher-esb-app.log
