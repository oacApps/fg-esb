package co.za.flash.esb.oneforyou.trader.restservice;

import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.model.request.ReversalsRedemptionRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.RefundResponseMdl;
import co.za.flash.esb.oneforyou.trader.model.response.ReversalsResponseMdl;
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
public class ReversalsWebService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*@Autowired*/
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flash.treasury.backend.reversals.prod}")
    private String reversalsProdEndpoint;

    @Value("${flash.treasury.backend.reversals.sandbox}")
    private String reversalsSandboxEndpoint;

    public ReversalsResponseMdl reversaleRestCall(HttpEntity<ReversalsRedemptionRequestMdl> reversalsRedemptionRequestMdl, String env, LoggingPayloadMdl loggingPayloadMdl)
    {
        ResponseEntity<ReversalsResponseMdl> responseEntity = null;
        String reversalsEndpoint = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(env) ? reversalsProdEndpoint : reversalsSandboxEndpoint;

        loggingPayloadMdl.setApiResource(reversalsEndpoint);

        try {
            responseEntity = restTemplate.exchange(reversalsEndpoint, HttpMethod.POST, reversalsRedemptionRequestMdl, ReversalsResponseMdl.class);
            if(null == responseEntity){
                ReversalsResponseMdl reversalsResponseMdl = new ReversalsResponseMdl();
                reversalsResponseMdl.setActionCode("101510");
                reversalsResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsResponseMdl);
                LOGGER.error("redeemRestCall Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                ReversalsResponseMdl reversalsResponseMdl = new ReversalsResponseMdl();
                reversalsResponseMdl.setActionCode("101510");
                reversalsResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsResponseMdl);
                LOGGER.error("redeemRestCall Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            ReversalsResponseMdl reversalsResponseMdl = new ReversalsResponseMdl();
            reversalsResponseMdl.setActionCode("101"+e.getRawStatusCode());
            reversalsResponseMdl.setScreenMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsResponseMdl);
            LOGGER.error("redeemRestCall HttpStatusCodeException: "+ e.getLocalizedMessage());
        } catch (RestClientException e){
            ReversalsResponseMdl reversalsResponseMdl = new ReversalsResponseMdl();
            reversalsResponseMdl.setActionCode("101500");
            reversalsResponseMdl.setScreenMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsResponseMdl);
            LOGGER.error("redeemRestCall RestClientException: "+ e.getLocalizedMessage());
        }finally {
            // DB logging
            LOGGER.info("Reversals Back end endpoint :"+reversalsEndpoint);
        }

        if (null == responseEntity.getBody()){
            ReversalsResponseMdl reversalsResponseMdl = new ReversalsResponseMdl();
            reversalsResponseMdl.setActionCode("101510");
            reversalsResponseMdl.setScreenMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsResponseMdl);
        }

        return responseEntity.getBody();
    }


}
