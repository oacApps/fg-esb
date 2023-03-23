package co.za.flash.esb.avtrouting.restservice;

import co.za.flash.esb.avtrouting.model.dto.request.RedeemRequestDTO;
import co.za.flash.esb.avtrouting.model.request.RedeemRequestMdl;
import co.za.flash.esb.avtrouting.model.response.RedeemResponseMdl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumeWebService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
   /* @Autowired*/
    RestTemplate restTemplate;

    public ConsumeWebService(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(35*1000);
        requestFactory.setReadTimeout(35*1000);
        restTemplate =  new RestTemplate(requestFactory);
    }

    public RedeemResponseMdl redeemService(HttpEntity<RedeemRequestMdl> entity, String backEndUrl)
    {
        ResponseEntity<RedeemResponseMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl , HttpMethod.POST, entity, RedeemResponseMdl.class);

            if(responseEntity == null){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setResponseCode(101510);
                redeemResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setResponseCode(101510);
                redeemResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("HttpStatusCodeException: " + e.getLocalizedMessage());
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setResponseCode(e.getRawStatusCode());
            redeemResponseMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
        } catch (RestClientException r) {
            LOGGER.error("RestClientException: " + r.getLocalizedMessage());
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setResponseCode(101504);
            redeemResponseMdl.setResponseMessage("Connection timed out");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
        } finally {
            LOGGER.info("backEndUrl: " + backEndUrl);
            LOGGER.info("backEndResponse: " + responseEntity.toString());
            // DB logging
        }
        return responseEntity.getBody();
    }

    public Object openAPIBackEnd(HttpEntity<RedeemRequestDTO> entity,
                                 String backEndUrl) {
        ResponseEntity<Object> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl,
                    HttpMethod.POST,
                    entity,
                    Object.class);
            if(responseEntity == null){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setResponseCode(101510);
                redeemResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity is null");
            }else if(responseEntity.getStatusCodeValue() == 0){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setResponseCode(101510);
                redeemResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e){
            LOGGER.error("HttpStatusCodeException: " + e.getLocalizedMessage());
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setResponseCode(e.getRawStatusCode());
            redeemResponseMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
        } catch (RestClientException r){
            LOGGER.error("RestClientException: "+r.getLocalizedMessage());
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setResponseCode(101504);
            redeemResponseMdl.setResponseMessage("Connection timed out");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
        } finally {
            LOGGER.info("spring boot OpenapiConditionalAvtRouting esb component");
            LOGGER.info("backEndUrl: " + backEndUrl);
        }
        return responseEntity.getBody();
    }
}
