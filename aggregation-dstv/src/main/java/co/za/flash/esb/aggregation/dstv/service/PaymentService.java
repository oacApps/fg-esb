package co.za.flash.esb.aggregation.dstv.service;

import co.za.flash.esb.aggregation.dstv.dto.request.PaymentRequestDTO;
import co.za.flash.esb.aggregation.dstv.error.Wso2Error;
import co.za.flash.esb.aggregation.dstv.mapper.OneBalanceResponseMapper;
import co.za.flash.esb.aggregation.dstv.mapper.PaymentRequestMapper;
import co.za.flash.esb.aggregation.dstv.model.DSTVRequestMdl;
import co.za.flash.esb.aggregation.dstv.model.OneBalance;
import co.za.flash.esb.aggregation.dstv.model.OneBalanceRefLong;
import co.za.flash.esb.aggregation.dstv.model.PaymentResponseMdl;
import co.za.flash.esb.aggregation.dstv.restclient.PaymentWebService;
import co.za.flash.esb.library.BuildHeader;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import com.google.gson.Gson;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${flash.debug}")
    private boolean isDebug;

    Wso2Error wso2Error = new Wso2Error();

    BuildHeader buildHeader = new BuildHeader();

    @Autowired
    PaymentWebService webService;

    public Object service(Map<String, String> headers, PaymentRequestDTO paymentRequestDTO, boolean isTest) {

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("dstv-lookup");
        loggingPayloadMdl.setRequestInPayload(paymentRequestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        JwtExtractor jwtExtractor = new JwtExtractor();
        Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

        PaymentResponseMdl responseMdl = new PaymentResponseMdl();

        try {

            if (isDebug) LOGGER.debug("Client Request Headers: " + headers.toString());

            if (headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                try {
                    jwtExtractor.extract(headers);

                    if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                        loggingPayloadMdl.setResponseOutPayload(wso2Error.invalidCredentials());
                        loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                        LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                        LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                        return wso2Error.invalidCredentials();
                    }

                    if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                        loggingPayloadMdl.setResponseOutPayload(wso2Error.invalidCredentials());
                        loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                        LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Key type missing.");
                        LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                        return wso2Error.invalidCredentials();
                    }

                } catch (JSONException | JWTException e) {
                    e.printStackTrace();
                    LOGGER.error("LookUp Service jwtExtractor JWTException: " + e.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("LookUp Service wso2JwtKeys: " + e.getLocalizedMessage());
                }

                HttpHeaders httpHeaders = buildHeader.build(headers);
                // rest of the code
                DSTVRequestMdl dstvRequestMdl = PaymentRequestMapper.INSTANCE.toMdl(paymentRequestDTO);
                long testSandBox = isTest ? 1L : 0L;
                dstvRequestMdl.setTrans_route_type(EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(jwtExtractor.getKeyType()) ? 0L : testSandBox );

                loggingPayloadMdl.setRequestOutPayload(dstvRequestMdl);
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                HttpEntity<DSTVRequestMdl> entity = new HttpEntity<>(dstvRequestMdl, httpHeaders);

                if(isDebug) LOGGER.debug("Entity sent to backend: " + JsonStringMapper.toJsonString(entity));
                String bkResponse = webService.backendService(entity, jwtExtractor.getKeyType(), loggingPayloadMdl);

                try{
                    Gson gson = new Gson();
                    responseMdl = gson.fromJson(bkResponse, PaymentResponseMdl.class);

                    if(null == responseMdl.getOBBillPaymentResponse()){
                        OneBalance oneBalance = gson.fromJson(bkResponse, OneBalance.class);
                        /*OBBillPaymentResponse obBillPaymentResponse = OneBalanceResponseMapper.INSTANCE.toOBBillPaymentResponse(oneBalance.getOneBalance());
                        obBillPaymentResponse.setAccountNumber(Long.valueOf(dstvRequestMdl.getAcc_num()));
                        responseMdl.setOBBillPaymentResponse(obBillPaymentResponse);*/
                        responseMdl.setOneBalance(oneBalance.getOneBalance());
                        loggingPayloadMdl.setResponseInPayload(OneBalanceResponseMapper.INSTANCE.toOneBalanceSM(oneBalance));
                    }else{
                        loggingPayloadMdl.setResponseInPayload(responseMdl);
                    }
                    loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

                    loggingPayloadMdl.setResponseOutPayload(responseMdl);
                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                } catch (Exception e){
                    LOGGER.error("gson exception: " + e.getMessage());
                    e.printStackTrace();
                    return new ResponseEntity("An internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                loggingPayloadMdl.setResponseOutPayload(wso2Error.jwtMissing());
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                return wso2Error.jwtMissing();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("LookUp Service : " + e.getLocalizedMessage());
            return new ResponseEntity("An internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return responseMdl;
    }
}
