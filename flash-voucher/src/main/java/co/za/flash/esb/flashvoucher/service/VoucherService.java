package co.za.flash.esb.flashvoucher.service;

import co.za.flash.esb.flashvoucher.dto.RequestDTO;
import co.za.flash.esb.flashvoucher.dto.ResponseDTO;
import co.za.flash.esb.flashvoucher.dto.TokenDTO;
import co.za.flash.esb.flashvoucher.mapper.RequestMapper;
import co.za.flash.esb.flashvoucher.mapper.ResponseMapper;
import co.za.flash.esb.flashvoucher.model.AuthenticationMDL;
import co.za.flash.esb.flashvoucher.model.RequestMDL;
import co.za.flash.esb.flashvoucher.model.ResponseMDL;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VoucherService {

    @Value("${flash.trader.treasury.endpoint.purchase.prod}")
    private String purchaseProdEndPoint;
    @Value("${flash.trader.treasury.endpoint.purchase.sandbox}")
    private String purchaseSandboxEndPoint;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

    @Autowired
    ConsumeWebService consumeWebService;

    public ResponseDTO purchase(Map<String, String> headers, RequestDTO purchaseDTO) {


        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        ResponseDTO responseDTO = new ResponseDTO();

        JwtExtractor jwtExtractor = new JwtExtractor();

        if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
            try {
                jwtExtractor.extract(headers);
            } catch (JWTException | JSONException e) {
                LOGGER.error("purchase jwtExtractor JWTException: " + e.getLocalizedMessage());
                LOGGER.info("purchase Request in: " + JsonStringMapper.toJsonString(purchaseDTO));
                e.printStackTrace();
            } catch (Exception e) {
                LOGGER.error("purchase jwtExtractor exception: " + e.getLocalizedMessage());
                LOGGER.info("purchase Request in: " + JsonStringMapper.toJsonString(purchaseDTO));
                e.printStackTrace();
            }

            if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                ResponseDTO refundResponse = new ResponseDTO();
                refundResponse.setResponseCode("900910");
                refundResponse.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                return refundResponse;
            }

            if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                ResponseDTO refundResponse = new ResponseDTO();
                refundResponse.setResponseCode("900910");
                refundResponse.setResponseMessage("Can not access the required resource with the provided access token. Key Type name missing.");
                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Key Type missing.");
                return refundResponse;
            }
        }else {
            ResponseDTO refundResponse = new ResponseDTO();
            refundResponse.setResponseCode("900401");
            refundResponse.setResponseMessage("Invalid credential");
            LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
            LOGGER.info("Request in: " + JsonStringMapper.toJsonString(purchaseDTO));
            return refundResponse;
        }

        String backEndUrl = "";
        backEndUrl = jwtExtractor.getKeyType().equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? purchaseProdEndPoint : purchaseSandboxEndPoint;


        loggingPayloadMdl.setApiKey("flash voucher-purchase");
        loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
        loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());
        loggingPayloadMdl.setApiResource(backEndUrl);

        loggingPayloadMdl.setRequestInPayload(purchaseDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());


        RequestMDL requestMDL = RequestMapper.INSTANCE.toMdl(purchaseDTO);
        AuthenticationMDL authenticationMDL = new AuthenticationMDL();
        authenticationMDL.setAuthenticated(true);
        requestMDL.getUser().setAuthentication(authenticationMDL);

        loggingPayloadMdl.setRequestOutPayload(requestMDL);
        loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        HttpEntity<RequestMDL> entity = new HttpEntity<RequestMDL>(requestMDL, httpHeaders);

        ResponseMDL responseMDL = consumeWebService.purchaseService(entity, backEndUrl);

        loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());
        loggingPayloadMdl.setResponseInPayload(responseMDL);

        responseDTO = ResponseMapper.INSTANCE.toDTO(responseMDL);

        TokenDTO tokenDTO = ResponseMapper.INSTANCE.toDTO(responseMDL.getCashVoucher());

        List<TokenDTO> tokenDTOList = new ArrayList<>();
        tokenDTOList.add(tokenDTO);
        responseDTO.setTokens(tokenDTOList);

        if(null!=responseDTO.getTransactionID()){
            responseDTO.setResponseMessage("Success");
        }
        responseDTO.setResponseTime(FlashDates.currentDateTimeFlashFormatInString());
        responseDTO.setAccountNumber(purchaseDTO.getAccountNumber());
        loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        loggingPayloadMdl.setResponseOutPayload(responseDTO);
        LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        return responseDTO;
    }
}
