package co.za.flash.esb.cellular.restservice;

import co.za.flash.esb.cellular.model.CellularRequestMdl;
import co.za.flash.esb.cellular.model.CellularResponseMdl;
import co.za.flash.esb.cellular.model.OBResponse;
import co.za.flash.esb.cellular.model.OneBalance;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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

    @Value("${flash.one.balance.backend.endpoint.production}")
    private String oneBalanceProduction;

    @Value("${flash.one.balance.backend.endpoint.sandbox}")
    private String oneBalanceSandbox;

    public String backendService(HttpEntity<CellularRequestMdl> entity, String environment, LoggingPayloadMdl loggingPayloadMdl)  {

        String oneBalanceURL = environment.equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? oneBalanceProduction : oneBalanceSandbox;
        loggingPayloadMdl.setApiResource(oneBalanceURL);

        OneBalance oneBalance = new OneBalance();
        OBResponse obResponse = new OBResponse();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity =  restTemplate.postForEntity(oneBalanceURL,entity, String.class);
            if(null == responseEntity){
                obResponse.setResponseCode(101510); // change this after consult with Torsten
                obResponse.setResponseMessage("Response processing failed");
                oneBalance.setOneBalance(obResponse);
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(obResponse.toString());
                LOGGER.error("Cellular Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                obResponse.setResponseCode(101510); // change this after consult with Torsten
                obResponse.setResponseMessage("Response processing failed");
                oneBalance.setOneBalance(obResponse);
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(obResponse.toString());
                LOGGER.error("Cellular Response processing failed (101510): responseEntity status code value is 0");
            }

        } catch (HttpStatusCodeException e) {
            LOGGER.error("Cellular HttpStatusCodeException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obResponse.setResponseCode(101000 + e.getRawStatusCode());
            obResponse.setResponseMessage(e.getStatusText());
            oneBalance.setOneBalance(obResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(obResponse.toString());
        } catch (RestClientException e){
            LOGGER.error("Cellular RestClientException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obResponse.setResponseCode(101500);
            obResponse.setResponseMessage("Internal Server error.");
            oneBalance.setOneBalance(obResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(obResponse.toString());
        } catch (RuntimeException e){
            LOGGER.error("Cellular RuntimeException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obResponse.setResponseCode(101500);
            obResponse.setResponseMessage("Internal Server error.");
            oneBalance.setOneBalance(obResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(obResponse.toString());
        } finally {
            LOGGER.info("backEndUrl: " + oneBalanceURL);
            LOGGER.info("Backend Response Entity : " + responseEntity.toString());
        }

        if (null == responseEntity.getBody()){
            obResponse.setResponseCode(101510);
            obResponse.setResponseMessage("Response processing failed");
            oneBalance.setOneBalance(obResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(obResponse.toString());
            LOGGER.error("Cellular Response processing failed (101510): responseEntity NULL");
        }
        return responseEntity.getBody();
    }

}
