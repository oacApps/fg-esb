package co.za.flash.esb.oneforyou.trader.service;

import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.dto.request.RefundRedemptionRequestDTO;
import co.za.flash.esb.oneforyou.trader.dblookup.CredentialLookupDTO;
import co.za.flash.esb.oneforyou.trader.dblookup.LookUpService;
import co.za.flash.esb.oneforyou.trader.dto.response.RefundRedemptionResponseDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.RefundRedemptionResponseMatchWsoDTO;
import co.za.flash.esb.oneforyou.trader.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.trader.mapper.RefundRequestMapper;
import co.za.flash.esb.oneforyou.trader.mapper.RefundResponseMapper;
import co.za.flash.esb.oneforyou.trader.model.request.RefundRedemptionRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.RefundResponseMdl;
import co.za.flash.esb.oneforyou.trader.model.shared.AcquirerMdl;
import co.za.flash.esb.oneforyou.trader.restservice.RefundWebService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RefundService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LookUpService lookUpService;

    private JwtExtractor jwtExtractor = new JwtExtractor();
    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();


    @Autowired
    RefundWebService refundWebService;

    public RefundRedemptionResponseMatchWsoDTO refundSvr(Map<String, String> headers, RefundRedemptionRequestDTO refundRedemptionRequestDTO){

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();

        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
        loggingPayloadMdl.setRequestInPayload(refundRedemptionRequestDTO);

        RefundRedemptionResponseDTO refundRedemptionResponseDTO = new RefundRedemptionResponseDTO();
        RefundRedemptionResponseMatchWsoDTO refundRedemptionResponseMatchWsoDTO = new RefundRedemptionResponseMatchWsoDTO();

        try {
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                jwtExtractor.extract(headers);
                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    refundRedemptionResponseMatchWsoDTO.setResponseCode("900910");
                    refundRedemptionResponseMatchWsoDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(refundRedemptionResponseMatchWsoDTO);
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    return refundRedemptionResponseMatchWsoDTO;
                }
                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    refundRedemptionResponseMatchWsoDTO.setResponseCode("900910");
                    refundRedemptionResponseMatchWsoDTO.setResponseMessage("Can not access the required resource with the provided access token. KeyType missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. KeyType missing.");
                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(refundRedemptionResponseMatchWsoDTO);
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    return refundRedemptionResponseMatchWsoDTO;
                }
            } else{
                refundRedemptionResponseMatchWsoDTO.setResponseCode("900401");
                refundRedemptionResponseMatchWsoDTO.setResponseMessage("Invalid credential");
                LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(refundRedemptionResponseMatchWsoDTO);
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                return refundRedemptionResponseMatchWsoDTO;
            }

        }catch (JSONException e) {
            LOGGER.error("jwtExtractor JSONException: " + e.getLocalizedMessage());
        } catch (JWTException e) {
            LOGGER.error("jwtExtractor JWTException: " + e.getLocalizedMessage());
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
        if(null != jwtExtractor.getKeyType()) {
            loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());
        }


        CredentialLookupDTO credentialLookupDTO=null;
        try {
            credentialLookupDTO = lookUpService.getByOrganizationAndKeyType(jwtExtractor.getOrganization(), jwtExtractor.getKeyType());
        }catch (RuntimeException e){
            LOGGER.warn("DB LookUpService  RuntimeException: " + e.getLocalizedMessage());
        }

        if (credentialLookupDTO == null) {
            refundRedemptionResponseMatchWsoDTO.setResponseCode("101404");
            refundRedemptionResponseMatchWsoDTO.setResponseMessage("Unable to process your request. Credential details not found.");
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(refundRedemptionResponseMatchWsoDTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.warn("Credential details not found: " + refundRedemptionRequestDTO.getAccountNumber());
        } else if(!credentialLookupDTO.isActive()){
            refundRedemptionResponseMatchWsoDTO.setResponseCode("101403");
            refundRedemptionResponseMatchWsoDTO.setResponseMessage("Unable to process your request. Credential details not found.");
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(refundRedemptionResponseMatchWsoDTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.warn("Credential details not found: " + refundRedemptionRequestDTO.getAccountNumber());
        }else {

            try {
                RefundRedemptionRequestMdl refundRedemptionRequestMdl = RefundRequestMapper.INSTANCE.toMdl(refundRedemptionRequestDTO);

                AcquirerMdl acquirer = new AcquirerMdl();
                refundRedemptionRequestMdl.setAcquirer(acquirer);

                ObjectMapper mapper = new ObjectMapper();
                try{
                    List<AcquirerDTO> acquirerDTOList = mapper.convertValue(refundRedemptionRequestDTO.getAcquirerReference(), new TypeReference<List<AcquirerDTO>>() { });
                    if(null != acquirerDTOList){
                        refundRedemptionRequestMdl.getAcquirer().setReference(acquirerDTOList.get(0).getValue());
                    }
                }catch (Exception e){
                    try {
                        AcquirerDTO acquirerDTO = mapper.convertValue(refundRedemptionRequestDTO.getAcquirerReference(), AcquirerDTO.class);
                        if (null != acquirerDTO) {
                            refundRedemptionRequestMdl.getAcquirer().setReference(acquirerDTO.getValue());
                        }
                    }catch (Exception ex){
                        LOGGER.warn("acquirer conversion on refund: " + ex.getMessage());
                    }
                }

                refundRedemptionRequestMdl.getAcquirer().setId(credentialLookupDTO.getAcquirerId());
                refundRedemptionRequestMdl.getAcquirer().setPassword(credentialLookupDTO.getAcquirerPassword());
                refundRedemptionRequestMdl.getUser().setAuthUserName(credentialLookupDTO.getAuthUserName());
                refundRedemptionRequestMdl.getUser().setAuthPassWord(credentialLookupDTO.getAuthPassword());

                HttpEntity<RefundRedemptionRequestMdl> refundRequestEntity = new HttpEntity<RefundRedemptionRequestMdl>(refundRedemptionRequestMdl, httpHeaders);

                loggingPayloadMdl.setRequestOutPayload(refundRedemptionRequestMdl);
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                RefundResponseMdl refundResponseMdl = refundWebService.refundRestCall(refundRequestEntity, jwtExtractor.getKeyType(), loggingPayloadMdl);
                loggingPayloadMdl.setResponseInPayload(refundResponseMdl);
                loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

                refundResponseMdl.setTransactionDate(FlashDates.currentDateTimeFlashFormatInString());
                refundResponseMdl.setSequenceNumber(refundRedemptionRequestMdl.getSequenceNumber());

                refundRedemptionResponseDTO = RefundResponseMapper.INSTANCE.toDTO(refundResponseMdl);
                if (refundRedemptionResponseDTO.getResponseCode().equals("0000")) {
                    refundRedemptionResponseDTO.setResponseMessage("Success");
                }
                refundRedemptionResponseDTO.setAcquirerReference(refundRedemptionRequestDTO.getAcquirerReference());

                refundRedemptionResponseMatchWsoDTO = RefundResponseMapper.INSTANCE.toWso2DTO(refundRedemptionResponseDTO);

                if(null != refundRedemptionResponseMatchWsoDTO.getChangeVoucher() && null != refundRedemptionResponseMatchWsoDTO.getChangeVoucher().getAmount()){
                    refundRedemptionResponseMatchWsoDTO.getChangeVoucher().getAmount().setCurrency("ZAR");
                }

                loggingPayloadMdl.setResponseOutPayload(refundRedemptionResponseMatchWsoDTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            }catch (Exception e){
                LOGGER.info("refundSvr: " + e.getMessage());
                refundRedemptionResponseMatchWsoDTO.setResponseCode("101403");
                refundRedemptionResponseMatchWsoDTO.setResponseMessage("Unable to process your request at this moment.");
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(refundRedemptionResponseMatchWsoDTO);
            }
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return refundRedemptionResponseMatchWsoDTO;
    }
}
