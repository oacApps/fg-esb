package co.za.flash.esb.oneforyou.trader.restservice;

import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.model.request.PurchaseRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.PurchaseResponseMdl;
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

import java.util.concurrent.TimeUnit;

@Service
public class PurchaseWebService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*@Autowired*/
    RestTemplate restTemplate = new RestTemplate();

    @Value("${flash.treasury.backend.purchase.prod}")
    private String purchaseProdEndpoint;

    @Value("${flash.treasury.backend.purchase.sandbox}")
    private String purchaseSandboxEndpoint;

    @Value("${flash.treasury.backend.vouchers.prod}")
    private String vouchersProdEndpoint;

    @Value("${flash.treasury.backend.vouchers.sandbox}")
    private String vouchersSandboxEndpoint;

    @Value("${flash.treasury.purchase.sleep}")
    private int treasurySleep;

    @Value("${flash.treasury.purchase.max.try}")
    private int treasuryMaxTry;

    public PurchaseResponseMdl purchaseRestCall(HttpEntity<PurchaseRequestMdl> purchaseRequestEntity, String env, LoggingPayloadMdl loggingPayloadMdl)
    {
        ResponseEntity<PurchaseResponseMdl> responseEntity = null;

        String purchaseEndpoint = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(env) ? purchaseProdEndpoint : purchaseSandboxEndpoint;
        String vouchersEndpoint = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(env) ? vouchersProdEndpoint : vouchersSandboxEndpoint;

        String backendForLog = "purchaseEndpoint: " + purchaseEndpoint + " ; vouchersEndpoint: " + vouchersEndpoint;
        loggingPayloadMdl.setApiResource(backendForLog);

        try {
            responseEntity = restTemplate.exchange(purchaseEndpoint, HttpMethod.POST, purchaseRequestEntity, PurchaseResponseMdl.class);
            if(null == responseEntity){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setActionCode("101510");
                purchaseResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("purchaseRestCall Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setActionCode("101510");
                purchaseResponseMdl.setScreenMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("purchaseRestCall Response processing failed (101510): responseEntity status code value is 0");
            }

           // if(!"0000".equalsIgnoreCase(responseEntity.getBody().getActionCode())) {
                int retried = 0;
                PurchaseResponseMdl purchaseResponse = responseEntity.getBody();
                LOGGER.info("Purchase Response Payload: " + JsonStringMapper.toJsonString(purchaseResponse));

                /**
                    We are reusing purchase request object for Voucher request
                    We added VoucherReference field to complete Voucher request payload
                **/
                if (null != purchaseResponse.getTransactionReference()) {
                    purchaseRequestEntity.getBody().setVoucherReference(purchaseResponse.getTransactionReference());
                }

                LOGGER.info("Vouchers Request Payload: " + JsonStringMapper.toJsonString(purchaseRequestEntity.getBody()));
                responseEntity = purchaseRetry(purchaseRequestEntity, vouchersEndpoint, retried);

                if (null != responseEntity && responseEntity.getBody().getActionCode().equals("9123")) {
                    retried = 1;
                    responseEntity = purchaseRetry(purchaseRequestEntity, vouchersEndpoint, retried);
                }
                LOGGER.info("Vouchers Response Payload: " + JsonStringMapper.toJsonString(responseEntity.getBody()));
            //}

        } catch (HttpStatusCodeException e) {
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setActionCode("101"+e.getRawStatusCode());
            purchaseResponseMdl.setScreenMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
            LOGGER.error("purchaseRestCall HttpStatusCodeException : " + e.getLocalizedMessage());
        } catch (RestClientException e){
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setActionCode("101500");
            purchaseResponseMdl.setScreenMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
            LOGGER.error("purchaseRestCall RestClientException : " + e.getLocalizedMessage());
        }finally {
            LOGGER.info("Purchase Back end endpoint :"+purchaseEndpoint);
            LOGGER.info("Vouchers Back end endpoint :"+vouchersEndpoint);
        }

        if (null == responseEntity.getBody()){
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setActionCode("101510");
            purchaseResponseMdl.setScreenMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
        }

        return responseEntity.getBody();
    }

    private ResponseEntity<PurchaseResponseMdl> purchaseRetry(HttpEntity<PurchaseRequestMdl> purchaseRequestEntity, String vouchersEndpoint, int retried){
        retried++;
        try {
            TimeUnit.MILLISECONDS.sleep(treasurySleep);
        } catch (InterruptedException e) {
            LOGGER.error("purchaseRetry InterruptedException" + e.getLocalizedMessage());
        }
        PurchaseResponseMdl purchaseResponseMdl = null;
        ResponseEntity<PurchaseResponseMdl> responseEntity = null;

        responseEntity = restTemplate.exchange(vouchersEndpoint, HttpMethod.POST, purchaseRequestEntity, PurchaseResponseMdl.class);

        if(retried <= treasuryMaxTry ) {
            if (null == responseEntity || null == responseEntity.getBody()) {
                responseEntity = purchaseRetry(purchaseRequestEntity, vouchersEndpoint, retried);
            } else {
                purchaseResponseMdl = responseEntity.getBody();
                if (purchaseResponseMdl.getActionCode().equals("9123")) {
                    responseEntity = purchaseRetry(purchaseRequestEntity, vouchersEndpoint, retried);
                }
            }
        }

        if(responseEntity == null){
            purchaseResponseMdl.setActionCode("101510");
            purchaseResponseMdl.setScreenMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
            LOGGER.error("purchaseRetry Response processing failed (101510): responseEntity is null");
        } else if(responseEntity.getStatusCodeValue() == 0){
            purchaseResponseMdl.setActionCode("101510");
            purchaseResponseMdl.setScreenMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
            LOGGER.error("purchaseRetry Response processing failed (101510): responseEntity status code value is 0");
        }

        return responseEntity;

    }

}
