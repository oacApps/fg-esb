package co.za.flash.esb.ricaregistration.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AgentStatusWebService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> agentRicaStatusBackendService(HttpEntity<String> entity, String smsURL)  {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity =  restTemplate.getForEntity(smsURL,String.class);
            if(null == responseEntity){
                LOGGER.error("Agent rica status Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                LOGGER.error("Agent rica status Response processing failed (101510): responseEntity status code value is 0");
            }

        } catch (HttpStatusCodeException e) {
            LOGGER.error("Agent rica status HttpStatusCodeException: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (RestClientException e){
            LOGGER.error("Agent rica status RestClientException: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (RuntimeException e){
            LOGGER.error("Agent rica status RuntimeException: " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            LOGGER.info("Agent rica status backEndUrl: " + smsURL);
        }

        return responseEntity;
    }

}
