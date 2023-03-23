package co.za.flash.esb.flashvoucher.service;

import co.za.flash.esb.flashvoucher.model.RequestMDL;
import co.za.flash.esb.flashvoucher.model.ResponseMDL;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /*@Autowired*/
    RestTemplate restTemplate = new RestTemplate();


    public ResponseMDL purchaseService(HttpEntity<RequestMDL> entity, String backEndUrl) {
        ResponseEntity<String> responseEntity = null;
        //ResponseEntity<String> response = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, String.class);

            if (null == responseEntity) {
                ResponseMDL purchaseResponseMdl = new ResponseMDL();
                purchaseResponseMdl.setActionCode("101510");
                purchaseResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl.toString());
                LOGGER.error("Purchase ConsumeWebService Response processing failed (101510): responseEntity is null");
                LOGGER.info("backend Response: " + responseEntity.toString());
            } else if (responseEntity.getStatusCodeValue() == 0) {
                ResponseMDL purchaseResponseMdl = new ResponseMDL();
                purchaseResponseMdl.setActionCode("101510");
                purchaseResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl.toString());
                LOGGER.error("Purchase ConsumeWebService Response processing failed (101510): responseEntity status code value is 0");
                LOGGER.info("backend Response: " + responseEntity.toString());
            }

        } catch (HttpStatusCodeException e) {
            ResponseMDL purchaseResponseMdl = new ResponseMDL();
            purchaseResponseMdl.setActionCode("101" + e.getRawStatusCode());
            purchaseResponseMdl.setScreenMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl.toString());
            LOGGER.error("Purchase ConsumeWebService HttpStatusCodeException : " + e.getLocalizedMessage());
            LOGGER.info("backend Response: " + responseEntity.toString());
        } catch (RestClientException r) {
            ResponseMDL purchaseResponseMdl = new ResponseMDL();
            purchaseResponseMdl.setActionCode("101500");
            purchaseResponseMdl.setScreenMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl.toString());
            LOGGER.error("Purchase ConsumeWebService RestClientException : " + r.getLocalizedMessage());
            LOGGER.info("backend Response: " + responseEntity.toString());
        }

        if (null == responseEntity.getBody()) {
            ResponseMDL purchaseResponseMdl = new ResponseMDL();
            purchaseResponseMdl.setActionCode("101510");
            purchaseResponseMdl.setScreenMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl.toString());
            LOGGER.error("Purchase ConsumeWebService responseEntity getBody NULL");
            LOGGER.info("backend Response: " + responseEntity.toString());
        }

        Gson gson = new Gson();
        ResponseMDL responseMDL = new ResponseMDL();
        try {
            responseMDL = gson.fromJson(responseEntity.getBody(), ResponseMDL.class);
        }catch (Exception e){
            LOGGER.error("Purchase ConsumeWebService Gson convertion failed");
            LOGGER.info("backend Response: " + responseEntity.getBody());
        }
        return responseMDL;
    }
}
