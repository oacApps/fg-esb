package co.za.flash.esb.electricity.restservice;

import co.za.flash.esb.electricity.model.request.LookupRequestMdl;
import co.za.flash.esb.electricity.model.request.PurchaseRequestMdl;
import co.za.flash.esb.electricity.model.response.LookupResponseMdl;
import co.za.flash.esb.electricity.model.response.PurchaseResponseMdl;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.DBLoggingPayload;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RestTemplate restTemplate;

    @Value("${flash.aggregation.backend.endpoint.purchase.prod}")
    private String purchaseProdEndpoint;

    @Value("${flash.aggregation.backend.endpoint.purchase.sandbox}")
    private String purchaseSandboxEndpoint;

    @Value("${flash.aggregation.backend.endpoint.Lookup.prod}")
    private String lookupProdEndpoint;

    @Value("${flash.aggregation.backend.endpoint.Lookup.sandbox}")
    private String lookupSandBoxEndpoint;

    public LookupResponseMdl lookupService(HttpEntity<LookupRequestMdl> entity, String environment, LoggingPayloadMdl loggingPayloadMdl){

        String lookupURL = environment.equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? lookupProdEndpoint : lookupSandBoxEndpoint;
        loggingPayloadMdl.setApiResource(lookupURL);

        ResponseEntity<LookupResponseMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(lookupURL, HttpMethod.POST, entity, LookupResponseMdl.class);

            if(null == responseEntity){
                LookupResponseMdl lookupResponseMdl = new LookupResponseMdl();
                lookupResponseMdl.setResponseCode(101510);
                lookupResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(lookupResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                LookupResponseMdl lookupResponseMdl = new LookupResponseMdl();
                lookupResponseMdl.setResponseCode(101510); // change this after consult with Torsten
                lookupResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(lookupResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("HttpStatusCodeException: "+e.getLocalizedMessage());
            LookupResponseMdl lookupResponseMdl = new LookupResponseMdl();
            lookupResponseMdl.setResponseCode(101000+e.getRawStatusCode());
            lookupResponseMdl.setMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(lookupResponseMdl);
        } catch (RestClientException r){
            LOGGER.error("RestClientException: " +r.getLocalizedMessage());
            LookupResponseMdl lookupResponseMdl = new LookupResponseMdl();
            lookupResponseMdl.setResponseCode(101504);
            lookupResponseMdl.setMessage("Connection timed out");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(lookupResponseMdl);
        } catch (RuntimeException e){
            LOGGER.error("oneForYouTraderRefundRedemption RuntimeException : " + e.getLocalizedMessage());
            LookupResponseMdl lookupResponseMdl = new LookupResponseMdl();
            lookupResponseMdl.setResponseCode(101500);
            lookupResponseMdl.setMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(lookupResponseMdl);
        }finally {
            LOGGER.info("backEndUrl: " + lookupURL);
        }
        return responseEntity.getBody();
    }

     public PurchaseResponseMdl purchaseRequestService(HttpEntity<PurchaseRequestMdl> entity, String environment, LoggingPayloadMdl loggingPayloadMdl){
         String purchaseURL = environment.equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? purchaseProdEndpoint : purchaseSandboxEndpoint;
         loggingPayloadMdl.setApiResource(purchaseURL);
         ResponseEntity<PurchaseResponseMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(purchaseURL, HttpMethod.POST, entity, PurchaseResponseMdl.class);

            if(responseEntity == null){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setResponseCode(101510);
                purchaseResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setResponseCode(101510); // change this after consult with Torsten
                purchaseResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error(e.getLocalizedMessage());
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101000+e.getRawStatusCode());
            purchaseResponseMdl.setMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
        } catch (RestClientException r){
            LOGGER.error(r.getLocalizedMessage());
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101504);
            purchaseResponseMdl.setMessage("Connection timed out");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
        }catch (RuntimeException e){
            LOGGER.error("oneForYouTraderRefundRedemption RuntimeException : " + e.getLocalizedMessage());
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101500);
            purchaseResponseMdl.setMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
        } finally {
            LOGGER.info("backEndUrl: " + purchaseURL);
        }
         return responseEntity.getBody();
    }

}
