package co.za.flash.esb.ricaregistration.restservice;

import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.ricaregistration.model.request.RegistrationsRequestMdl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationWebService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*@Autowired*/
    RestTemplate restTemplate = new RestTemplate();

    public String registrationRestCall(HttpEntity<RegistrationsRequestMdl> registrationsRequest, String registrationURL)
    {
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = restTemplate.postForEntity(registrationURL,  registrationsRequest, String.class);
            if(null == responseEntity){
                LOGGER.error("Registration Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                LOGGER.error("Registration Response processing failed (101510): responseEntity status code value is 0");
            }

        } catch (HttpStatusCodeException e) {
            LOGGER.error("Agent rica Registration HttpStatusCodeException: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (RestClientException e){
            LOGGER.error("Agent rica Registration RestClientException: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (RuntimeException e){
            LOGGER.error("Agent rica Registration RuntimeException: " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            LOGGER.info("Agent rica Registration backEndUrl: " + registrationURL);
        }

        return responseEntity.getBody();
    }


}
