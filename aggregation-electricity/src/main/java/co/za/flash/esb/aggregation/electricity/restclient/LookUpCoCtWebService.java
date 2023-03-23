package co.za.flash.esb.aggregation.electricity.restclient;

import co.za.flash.esb.aggregation.electricity.model.request.ElectricityRequestMdl;
import co.za.flash.esb.aggregation.electricity.model.response.LookUpCoCTResponseMdl;
import co.za.flash.esb.aggregation.electricity.model.response.OBElecLookupResponse;
import co.za.flash.esb.aggregation.electricity.model.response.OBElecResponse;
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
public class LookUpCoCtWebService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    @Value("${flash.one.balance.backend.endpoint.production}")
    private String oneBalanceProduction;

    @Value("${flash.one.balance.backend.endpoint.sandbox}")
    private String oneBalanceSandbox;

    public String backendService(HttpEntity<ElectricityRequestMdl> entity, String environment, LoggingPayloadMdl loggingPayloadMdl)  {

        String oneBalanceURL = environment.equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? oneBalanceProduction : oneBalanceSandbox;
        loggingPayloadMdl.setApiResource(oneBalanceURL);

        OBElecLookupResponse obElecResponse = new OBElecLookupResponse();
        LookUpCoCTResponseMdl responseMdl = new LookUpCoCTResponseMdl();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity =  restTemplate.postForEntity(oneBalanceURL,entity, String.class);
            if(null == responseEntity){
                obElecResponse.setResponseCode(101510);
                obElecResponse.setResponseMessage("Response processing failed");
                responseMdl.setOBElecResponse(obElecResponse);
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
                LOGGER.error("LookUpCoCTWebService Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                obElecResponse.setResponseCode(101510);
                obElecResponse.setResponseMessage("Response processing failed");
                responseMdl.setOBElecResponse(obElecResponse);
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
                LOGGER.error("LookUpWebService Response processing failed (101510): responseEntity status code value is 0");
            }

        } catch (HttpStatusCodeException e) {
            LOGGER.error("LookUpCoCTWebService HttpStatusCodeException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obElecResponse.setResponseCode(101000 + e.getRawStatusCode());
            obElecResponse.setResponseMessage("Response processing failed");
            responseMdl.setOBElecResponse(obElecResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
        } catch (RestClientException e){
            LOGGER.error("LookUpCoCTWebService RestClientException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obElecResponse.setResponseCode(101500);
            obElecResponse.setResponseMessage("Internal Server error.");
            responseMdl.setOBElecResponse(obElecResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
        } catch (RuntimeException e){
            LOGGER.error("LookUpCoCTWebService RuntimeException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obElecResponse.setResponseCode(101500);
            obElecResponse.setResponseMessage("Internal Server error.");
            responseMdl.setOBElecResponse(obElecResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
        } finally {
            LOGGER.info("backEndUrl: " + oneBalanceURL);
            LOGGER.info("Backend Response Entity : " + responseEntity.toString());
        }

        if (null == responseEntity.getBody()){
            obElecResponse.setResponseCode(101510);
            obElecResponse.setResponseMessage("Response processing failed");
            responseMdl.setOBElecResponse(obElecResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
            LOGGER.error("LookUpCoCTWebService Response processing failed (101510): responseEntity NULL");
        }
        return responseEntity.getBody();
    }

}
