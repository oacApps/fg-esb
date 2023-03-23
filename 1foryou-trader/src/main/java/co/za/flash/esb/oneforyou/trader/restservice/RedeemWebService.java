package co.za.flash.esb.oneforyou.trader.restservice;

import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.model.request.RedeemRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.PurchaseResponseMdl;
import co.za.flash.esb.oneforyou.trader.model.response.RedeemResponseMdl;
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
public class RedeemWebService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*@Autowired*/
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flash.treasury.backend.redeem.prod}")
    private String redeemProdEndpoint;

    @Value("${flash.treasury.backend.redeem.sandbox}")
    private String redeemSandboxEndpoint;

    public RedeemResponseMdl redeemRestCall(HttpEntity<RedeemRequestMdl> redeemRequestEntity, String env, LoggingPayloadMdl loggingPayloadMdl)
    {
        ResponseEntity<RedeemResponseMdl> responseEntity = null;
        String redeemEndpoint = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(env) ? redeemProdEndpoint : redeemSandboxEndpoint;

        loggingPayloadMdl.setApiResource(redeemEndpoint);

        try {
            responseEntity = restTemplate.exchange(redeemEndpoint, HttpMethod.POST, redeemRequestEntity, RedeemResponseMdl.class);
            if(null == responseEntity){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setActionCode("101510");
                redeemResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("redeemRestCall Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setActionCode("101510");
                redeemResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("redeemRestCall Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setActionCode("101"+e.getRawStatusCode());
            redeemResponseMdl.setScreenMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
            LOGGER.error("redeemRestCall HttpStatusCodeException : " + e.getLocalizedMessage());
        } catch (RestClientException e){
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setActionCode("101500");
            redeemResponseMdl.setScreenMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
            LOGGER.error("redeemRestCall RestClientException : " + e.getLocalizedMessage());
        }finally {
            // DB logging
            LOGGER.info("Redeem Back end endpoint :"+redeemEndpoint);
        }

        if (null == responseEntity.getBody()){
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setActionCode("101510");
            redeemResponseMdl.setScreenMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
        }

        return responseEntity.getBody();
    }


}
