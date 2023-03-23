package co.za.flash.esb.oneforyou.trader.restservice;

import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.model.request.RefundRedemptionRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.RedeemResponseMdl;
import co.za.flash.esb.oneforyou.trader.model.response.RefundResponseMdl;
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
public class RefundWebService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*@Autowired*/
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flash.treasury.backend.refunds.prod}")
    private String refundsProdEndpoint;

    @Value("${flash.treasury.backend.refunds.sandbox}")
    private String refundsSandboxEndpoint;

    public RefundResponseMdl refundRestCall(HttpEntity<RefundRedemptionRequestMdl> refundRedemptionRequestMdl, String env, LoggingPayloadMdl loggingPayloadMdl)
    {
        ResponseEntity<RefundResponseMdl> responseEntity = null;
        String refundsEndpoint = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(env) ? refundsProdEndpoint : refundsSandboxEndpoint;

        loggingPayloadMdl.setApiResource(refundsEndpoint);

        try {
            responseEntity = restTemplate.exchange(refundsEndpoint, HttpMethod.POST, refundRedemptionRequestMdl, RefundResponseMdl.class);
            if(null == responseEntity){
                RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
                refundResponseMdl.setActionCode("101510");
                refundResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
                LOGGER.error("refundRestCall Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
                refundResponseMdl.setActionCode("101510");
                refundResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
                LOGGER.error("refundRestCall Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
            refundResponseMdl.setActionCode("101"+e.getRawStatusCode());
            refundResponseMdl.setScreenMessage(e.getStatusText().isEmpty()? "101"+e.getRawStatusCode()+": Request processing failed" : e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
            LOGGER.error("refundRestCall HttpStatusCodeException: " + e.getLocalizedMessage());
        } catch (RestClientException e){
            RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
            refundResponseMdl.setActionCode("101500");
            refundResponseMdl.setScreenMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
            LOGGER.error("refundRestCall RestClientException: " + e.getLocalizedMessage());
        }finally {
            // DB logging
            LOGGER.info("Refund Back end endpoint :"+refundsEndpoint);
        }

        if (null == responseEntity.getBody()){
            RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
            refundResponseMdl.setActionCode("101510");
            refundResponseMdl.setScreenMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
        }

        return responseEntity.getBody();
    }


}
