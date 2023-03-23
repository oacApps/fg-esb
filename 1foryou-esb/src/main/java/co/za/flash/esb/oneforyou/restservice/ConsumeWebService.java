package co.za.flash.esb.oneforyou.restservice;

import co.za.flash.esb.library.pojo.DBLoggingPayload;
import co.za.flash.esb.oneforyou.model.SequenceMdl;
import co.za.flash.esb.oneforyou.model.request.PurchaseRequestMdl;
import co.za.flash.esb.oneforyou.model.request.RedeemRequestMdl;
import co.za.flash.esb.oneforyou.model.request.RefundRequestMdl;
import co.za.flash.esb.oneforyou.model.request.trader.PurchaseRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.RedeemRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.RefundRedemptionRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.ReverseRedemptionRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.response.PurchaseResponseMdl;
import co.za.flash.esb.oneforyou.model.response.RedeemResponseMdl;
import co.za.flash.esb.oneforyou.model.response.RefundResponseMdl;
import co.za.flash.esb.oneforyou.model.response.trader.PurchaseResponseTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.RedeemResponseTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.RefundRedemptionResponseTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.ReversalsRedemptionResponseTraderMdl;
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



    public PurchaseResponseMdl avtPurchaseService(HttpEntity<PurchaseRequestMdl> entity, String backEndUrl)
    {
        ResponseEntity<PurchaseResponseMdl> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, PurchaseResponseMdl.class);

            if(null == responseEntity){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setResponseCode(101510);
                purchaseResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("avtPurchase ConsumeWebService Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
                purchaseResponseMdl.setResponseCode(101510);
                purchaseResponseMdl.setMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
                LOGGER.error("avtPurchase ConsumeWebService Response processing failed (101510): responseEntity status code value is 0");
            }

        } catch (HttpStatusCodeException e) {
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101000+e.getRawStatusCode());
            purchaseResponseMdl.setMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
            LOGGER.error("avtPurchase ConsumeWebService HttpStatusCodeException : " + e.getLocalizedMessage());
        } catch (RestClientException r){
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101500);
            purchaseResponseMdl.setMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
            LOGGER.error("avtPurchase ConsumeWebService RestClientException : " + r.getLocalizedMessage());
        }

        if (null == responseEntity.getBody()){
            PurchaseResponseMdl purchaseResponseMdl = new PurchaseResponseMdl();
            purchaseResponseMdl.setResponseCode(101510);
            purchaseResponseMdl.setMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseMdl);
            LOGGER.error("avtPurchase ConsumeWebService responseEntity getBody NULL");
        }
        return responseEntity.getBody();
    }



    public RedeemResponseMdl redeemService(HttpEntity<RedeemRequestMdl> entity, String backEndUrl)
    {
        ResponseEntity<RedeemResponseMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, RedeemResponseMdl.class);
            if(null == responseEntity){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setResponseCode(101510); // change this after consult with Torsten
                redeemResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("redeemService Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
                redeemResponseMdl.setResponseCode(101510); // change this after consult with Torsten
                redeemResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
                LOGGER.error("redeemService Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("redeemService HttpStatusCodeException : " + e.getLocalizedMessage());
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setResponseCode(101000+e.getRawStatusCode());
            redeemResponseMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
        } catch (RestClientException e){
            LOGGER.error("redeemService RestClientException : " + e.getLocalizedMessage());
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setResponseCode(101500);
            redeemResponseMdl.setResponseMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
        } finally {
            LOGGER.info("backEndUrl: " + backEndUrl);
        }

        if (null == responseEntity.getBody()){
            RedeemResponseMdl redeemResponseMdl = new RedeemResponseMdl();
            redeemResponseMdl.setResponseCode(101510); // change this after consult with Torsten
            redeemResponseMdl.setResponseMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseMdl);
            LOGGER.warn("redeemService Response processing failed (101510): responseEntity NULL");
        }
        return responseEntity.getBody();
    }

    public RefundResponseMdl refundService(HttpEntity<RefundRequestMdl> entity, String backEndUrl)
    {
        ResponseEntity<RefundResponseMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, RefundResponseMdl.class);
            if(null == responseEntity){
                RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
                refundResponseMdl.setResponseCode(101510); // change this after consult with Torsten
                refundResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
                refundResponseMdl.setResponseCode(101510); // change this after consult with Torsten
                refundResponseMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
                LOGGER.error("Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("refundService HttpStatusCodeException : " + e.getLocalizedMessage());
            RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
            refundResponseMdl.setResponseCode(101000+e.getRawStatusCode());
            refundResponseMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
        } catch (RestClientException e){
            LOGGER.error("refundService RestClientException : " + e.getLocalizedMessage());
            RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
            refundResponseMdl.setResponseCode(101500);
            refundResponseMdl.setResponseMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
        }finally {
            LOGGER.info("backEndUrl: " + backEndUrl);
        }

        if (null == responseEntity.getBody()){
            RefundResponseMdl refundResponseMdl = new RefundResponseMdl();
            refundResponseMdl.setResponseCode(101510); // change this after consult with Torsten
            refundResponseMdl.setResponseMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundResponseMdl);
            LOGGER.error("Response processing failed (101510): responseEntity NULL");
        }

        return responseEntity.getBody();
    }

    public PurchaseResponseTraderMdl oneForYouTraderPurchase(HttpEntity<PurchaseRequestTraderMdl> entity,
                                                             String backEndUrl) {
        ResponseEntity<PurchaseResponseTraderMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, PurchaseResponseTraderMdl.class);
            if(null == responseEntity){
                PurchaseResponseTraderMdl purchaseResponseTraderMdl = new PurchaseResponseTraderMdl();
                purchaseResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                purchaseResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseTraderMdl);
                LOGGER.error("oneForYouTraderPurchase Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                PurchaseResponseTraderMdl purchaseResponseTraderMdl = new PurchaseResponseTraderMdl();
                purchaseResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                purchaseResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseTraderMdl);
                LOGGER.error("oneForYouTraderPurchase Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("oneForYouTraderPurchase HttpStatusCodeException : " + e.getLocalizedMessage());
            PurchaseResponseTraderMdl purchaseResponseTraderMdl = new PurchaseResponseTraderMdl();
            purchaseResponseTraderMdl.setResponseCode("101"+ e.getRawStatusCode());
            purchaseResponseTraderMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseTraderMdl);
        } catch (RestClientException e){
            LOGGER.error("oneForYouTraderPurchase RestClientException : " + e.getLocalizedMessage());
            PurchaseResponseTraderMdl purchaseResponseTraderMdl = new PurchaseResponseTraderMdl();
            purchaseResponseTraderMdl.setResponseCode("101500");
            purchaseResponseTraderMdl.setResponseMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseTraderMdl);
        }finally {
            LOGGER.info("backEndUrl: " + backEndUrl);
        }

        if (null == responseEntity.getBody()){
            PurchaseResponseTraderMdl purchaseResponseTraderMdl = new PurchaseResponseTraderMdl();
            purchaseResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
            purchaseResponseTraderMdl.setResponseMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(purchaseResponseTraderMdl);
            LOGGER.error("oneForYouTraderPurchase Response processing failed (101510): responseEntity is null");
        }

        return responseEntity.getBody();
    }

    public RedeemResponseTraderMdl oneForYouTraderRedeem(HttpEntity<RedeemRequestTraderMdl> entity,
                                                         String backEndUrl) {
        ResponseEntity<RedeemResponseTraderMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, RedeemResponseTraderMdl.class);
            if(null == responseEntity){
                RedeemResponseTraderMdl redeemResponseTraderMdl = new RedeemResponseTraderMdl();
                redeemResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                redeemResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseTraderMdl);
                LOGGER.error("oneForYouTraderRedeem Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                RedeemResponseTraderMdl redeemResponseTraderMdl = new RedeemResponseTraderMdl();
                redeemResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                redeemResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseTraderMdl);
                LOGGER.error("oneForYouTraderRedeem Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("oneForYouTraderRedeem HttpStatusCodeException : " + e.getLocalizedMessage());
            RedeemResponseTraderMdl redeemResponseTraderMdl = new RedeemResponseTraderMdl();
            redeemResponseTraderMdl.setResponseCode("101"+ e.getRawStatusCode());
            redeemResponseTraderMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseTraderMdl);
        } catch (RestClientException e){
            LOGGER.error("oneForYouTraderRedeem RestClientException : " + e.getLocalizedMessage());
            RedeemResponseTraderMdl redeemResponseTraderMdl = new RedeemResponseTraderMdl();
            redeemResponseTraderMdl.setResponseCode("101500");
            redeemResponseTraderMdl.setResponseMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseTraderMdl);
        }finally {
            LOGGER.info("backEndUrl: " + backEndUrl);
        }

        if (null == responseEntity.getBody()){
            RedeemResponseTraderMdl redeemResponseTraderMdl = new RedeemResponseTraderMdl();
            redeemResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
            redeemResponseTraderMdl.setResponseMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(redeemResponseTraderMdl);
            LOGGER.error("oneForYouTraderRedeem Response processing failed (101510): responseEntity NULL");
        }
        return responseEntity.getBody();
    }

    public RefundRedemptionResponseTraderMdl oneForYouTraderRefundRedemption(HttpEntity<RefundRedemptionRequestTraderMdl> entity,
                                                                             String backEndUrl) {
        ResponseEntity<RefundRedemptionResponseTraderMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, RefundRedemptionResponseTraderMdl.class);
            if(null == responseEntity){
                RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl = new RefundRedemptionResponseTraderMdl();
                refundRedemptionResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                refundRedemptionResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundRedemptionResponseTraderMdl);
                LOGGER.error("oneForYouTraderRefundRedemption Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl = new RefundRedemptionResponseTraderMdl();
                refundRedemptionResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                refundRedemptionResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundRedemptionResponseTraderMdl);
                LOGGER.error("oneForYouTraderRefundRedemption Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("oneForYouTraderRefundRedemption HttpStatusCodeException : " + e.getLocalizedMessage());
            RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl = new RefundRedemptionResponseTraderMdl();
            refundRedemptionResponseTraderMdl.setResponseCode("101"+ e.getRawStatusCode());
            refundRedemptionResponseTraderMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundRedemptionResponseTraderMdl);
        } catch (RestClientException e){
            LOGGER.error("oneForYouTraderRefundRedemption RestClientException : " + e.getLocalizedMessage());
            RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl = new RefundRedemptionResponseTraderMdl();
            refundRedemptionResponseTraderMdl.setResponseCode("101500");
            refundRedemptionResponseTraderMdl.setResponseMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundRedemptionResponseTraderMdl);
        }catch (RuntimeException e){
            LOGGER.error("oneForYouTraderRefundRedemption RuntimeException : " + e.getLocalizedMessage());
            RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl = new RefundRedemptionResponseTraderMdl();
            refundRedemptionResponseTraderMdl.setResponseCode("101500");
            refundRedemptionResponseTraderMdl.setResponseMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundRedemptionResponseTraderMdl);
        }finally {
            LOGGER.info("backEndUrl: " + backEndUrl);
        }

        if (null == responseEntity.getBody()){
            RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl = new RefundRedemptionResponseTraderMdl();
            refundRedemptionResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
            refundRedemptionResponseTraderMdl.setResponseMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(refundRedemptionResponseTraderMdl);
        }

        return responseEntity.getBody();
    }

    public ReversalsRedemptionResponseTraderMdl oneForYouTraderReverseRedemption(HttpEntity<ReverseRedemptionRequestTraderMdl> entity,
                                                                                 String backEndUrl) {
        ResponseEntity<ReversalsRedemptionResponseTraderMdl> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl, HttpMethod.POST, entity, ReversalsRedemptionResponseTraderMdl.class);
            if(null == responseEntity){
                ReversalsRedemptionResponseTraderMdl reversalsRedemptionResponseTraderMdl = new ReversalsRedemptionResponseTraderMdl();
                reversalsRedemptionResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                reversalsRedemptionResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsRedemptionResponseTraderMdl);
                LOGGER.error("oneForYouTraderReverseRedemption Response processing failed (101510): responseEntity is null");
            } else if(responseEntity.getStatusCodeValue() == 0){
                ReversalsRedemptionResponseTraderMdl reversalsRedemptionResponseTraderMdl = new ReversalsRedemptionResponseTraderMdl();
                reversalsRedemptionResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
                reversalsRedemptionResponseTraderMdl.setResponseMessage("Response processing failed");
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsRedemptionResponseTraderMdl);
                LOGGER.error("oneForYouTraderReverseRedemption Response processing failed (101510): responseEntity status code value is 0");
            }
        } catch (HttpStatusCodeException e) {
            LOGGER.error("oneForYouTraderReverseRedemption HttpStatusCodeException : " + e.getLocalizedMessage());
            ReversalsRedemptionResponseTraderMdl reversalsRedemptionResponseTraderMdl = new ReversalsRedemptionResponseTraderMdl();
            reversalsRedemptionResponseTraderMdl.setResponseCode("101"+ e.getRawStatusCode());
            reversalsRedemptionResponseTraderMdl.setResponseMessage(e.getStatusText());
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsRedemptionResponseTraderMdl);
        } catch (RestClientException e){
            LOGGER.error("oneForYouTraderReverseRedemption RestClientException : " + e.getLocalizedMessage());
            ReversalsRedemptionResponseTraderMdl reversalsRedemptionResponseTraderMdl = new ReversalsRedemptionResponseTraderMdl();
            reversalsRedemptionResponseTraderMdl.setResponseCode("101500");
            reversalsRedemptionResponseTraderMdl.setResponseMessage("Internal Server error.");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsRedemptionResponseTraderMdl);
        }finally {
            LOGGER.info("backEndUrl: " + backEndUrl);
        }

        if (null == responseEntity.getBody()){
            ReversalsRedemptionResponseTraderMdl reversalsRedemptionResponseTraderMdl = new ReversalsRedemptionResponseTraderMdl();
            reversalsRedemptionResponseTraderMdl.setResponseCode("101510"); // change this after consult with Torsten
            reversalsRedemptionResponseTraderMdl.setResponseMessage("Response processing failed");
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(reversalsRedemptionResponseTraderMdl);
        }


        return responseEntity.getBody();
    }

    public Long oneForYouTraderSequenceNumber(HttpEntity<SequenceMdl> entity,
                                                    String backEndUrl) {
        ResponseEntity<Long> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(backEndUrl,
                    HttpMethod.POST,
                    entity,
                    Long.class);
        } catch (HttpStatusCodeException e) {
            LOGGER.error("oneForYouTraderSequenceNumber HttpStatusCodeException: " + e.getLocalizedMessage());
        } catch (RestClientException r){
            LOGGER.error(" oneForYouTraderSequenceNumber RestClientException: " + r.getLocalizedMessage());
        } finally {
            LOGGER.info("spring boot 1foryou trader esb component");
            LOGGER.info("backEndUrl: " + backEndUrl);
        }
        return (null != responseEntity) ? responseEntity.getBody() : 0L;
    }
}
