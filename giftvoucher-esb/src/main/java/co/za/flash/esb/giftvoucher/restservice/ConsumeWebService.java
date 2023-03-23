package co.za.flash.esb.giftvoucher.restservice;

import co.za.flash.esb.giftvoucher.model.PurchaseRequestMdl;
import co.za.flash.esb.giftvoucher.model.PurchaseResponseMdl;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumeWebService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    RestTemplate restTemplate = new RestTemplate();

    @Value("${flash.aggregation.backend.incomm.purchase.prod}")
    private String aggregationInCommProd;

    @Value("${flash.aggregation.backend.incomm.purchase.sandbox}")
    private String aggregationInCommSandBox;

    public PurchaseResponseMdl purchaseRequestService(HttpEntity<PurchaseRequestMdl> entity, String env, LoggingPayloadMdl loggingPayloadMdl){

        String backEndUrl = env.equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? aggregationInCommProd : aggregationInCommSandBox;
        loggingPayloadMdl.setApiResource(backEndUrl);

        ResponseEntity<PurchaseResponseMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, PurchaseResponseMdl.class);
            if(null == responseEntity){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setResponseCode(101510);
                purchaseResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity is null");
            } else if(null == responseEntity.getBody()){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setResponseCode(101510);
                purchaseResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity body is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setResponseCode(101510); // change this after consult with Torsten
                purchaseResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException ex) {
            LOGGER.error("HttpStatusCodeException: " + ex.getLocalizedMessage());
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101000+ex.getRawStatusCode());
            purchaseResponseMdl.setMessage(ex.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
        } catch (RestClientException e) {
            LOGGER.error("RestClientException: " + e.getLocalizedMessage());
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101504);
            purchaseResponseMdl.setMessage("Connection timed out");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
        }

        return responseEntity.getBody();
    }
}
