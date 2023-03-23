package co.za.flash.esb.aggregation.payat.restclient;

import co.za.flash.esb.aggregation.payat.model.request.PayAtReqMdl;
import co.za.flash.esb.aggregation.payat.model.response.LookUpResponseMdl;
import co.za.flash.esb.aggregation.payat.model.response.OBBillPaymentResponse;
import co.za.flash.esb.aggregation.payat.model.response.OBBillPaymentResponseRefString;
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
public class LookUpWebService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    @Value("${flash.one.balance.backend.endpoint.production}")
    private String oneBalanceProduction;

    @Value("${flash.one.balance.backend.endpoint.sandbox}")
    private String oneBalanceSandbox;

    public String backendService(HttpEntity<PayAtReqMdl> entity, String environment, LoggingPayloadMdl loggingPayloadMdl)  {

        String oneBalanceURL = environment.equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? oneBalanceProduction : oneBalanceSandbox;
        loggingPayloadMdl.setApiResource(oneBalanceURL);

        OBBillPaymentResponseRefString obBillPaymentResponse = new OBBillPaymentResponseRefString();
        LookUpResponseMdl responseMdl = new LookUpResponseMdl();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity =  restTemplate.postForEntity(oneBalanceURL,entity, String.class);
            if(null == responseEntity){
                obBillPaymentResponse.setResponseCode(101510);
                obBillPaymentResponse.setResponseMessage("Response processing failed");
                responseMdl.setOBBillPaymentResponse(obBillPaymentResponse);
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
                LOGGER.error("LookUpWebService Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                obBillPaymentResponse.setResponseCode(101510);
                obBillPaymentResponse.setResponseMessage("Response processing failed");
                responseMdl.setOBBillPaymentResponse(obBillPaymentResponse);
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
                LOGGER.error("LookUpWebService Response processing failed (101510): responseEntity status code value is 0");
            }

        } catch (HttpStatusCodeException e) {
            LOGGER.error("LookUpWebService HttpStatusCodeException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obBillPaymentResponse.setResponseCode(101000 + e.getRawStatusCode());
            obBillPaymentResponse.setResponseMessage(e.getStatusText());
            responseMdl.setOBBillPaymentResponse(obBillPaymentResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
        } catch (RestClientException e){
            LOGGER.error("LookUpWebService RestClientException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obBillPaymentResponse.setResponseCode(101500);
            obBillPaymentResponse.setResponseMessage("Internal Server error.");
            responseMdl.setOBBillPaymentResponse(obBillPaymentResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
        } catch (RuntimeException e){
            LOGGER.error("LookUpWebService RuntimeException : " + e.getLocalizedMessage());
            LOGGER.info("Response Entity : " + responseEntity.toString());
            e.printStackTrace();
            obBillPaymentResponse.setResponseCode(101500);
            obBillPaymentResponse.setResponseMessage("Internal Server error.");
            responseMdl.setOBBillPaymentResponse(obBillPaymentResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
        } finally {
            LOGGER.info("backEndUrl: " + oneBalanceURL);
            LOGGER.info("Backend Response Entity : " + responseEntity.toString());
        }

        if (null == responseEntity.getBody()){
            obBillPaymentResponse.setResponseCode(101510);
            obBillPaymentResponse.setResponseMessage("Response processing failed");
            responseMdl.setOBBillPaymentResponse(obBillPaymentResponse);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseMdl.toString());
            LOGGER.error("LookUpWebService Response processing failed (101510): responseEntity NULL");
        }
        return responseEntity.getBody();
    }

}
