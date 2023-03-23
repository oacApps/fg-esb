package co.za.flash.esb.oneforyou.trader.service;

import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.dto.request.RedeemRequestDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.RedeemResponseMatchWsoDTO;
import co.za.flash.esb.oneforyou.trader.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.trader.model.shared.AcquirerMdl;
import co.za.flash.esb.oneforyou.trader.restservice.RedeemWebService;
import co.za.flash.esb.oneforyou.trader.dblookup.CredentialLookupDTO;
import co.za.flash.esb.oneforyou.trader.dblookup.LookUpService;
import co.za.flash.esb.oneforyou.trader.dto.response.RedeemResponseDTO;
import co.za.flash.esb.oneforyou.trader.mapper.RedeemRequestMapper;
import co.za.flash.esb.oneforyou.trader.mapper.RedeemResponseMapper;
import co.za.flash.esb.oneforyou.trader.model.request.RedeemRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.RedeemResponseMdl;
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
public class RedeemService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LookUpService lookUpService;

    private JwtExtractor jwtExtractor = new JwtExtractor();
    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();


    @Autowired
    RedeemWebService redeemWebService;

    public RedeemResponseMatchWsoDTO redeemSvr(Map<String, String> headers, RedeemRequestDTO redeemRequestDTO){
        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();

        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
        loggingPayloadMdl.setRequestInPayload(redeemRequestDTO);

        RedeemResponseMatchWsoDTO redeemResponseMatchWso2DTO = new RedeemResponseMatchWsoDTO();
        RedeemResponseDTO redeemResponseDTO = new RedeemResponseDTO();

        try {
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                jwtExtractor.extract(headers);
                if (null == jwtExtractor.getOrganization()|| jwtExtractor.getOrganization().isEmpty()) {
                    redeemResponseMatchWso2DTO.setResponseCode("900910");
                    redeemResponseMatchWso2DTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(redeemResponseMatchWso2DTO);
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    return redeemResponseMatchWso2DTO;
                }
                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    redeemResponseMatchWso2DTO.setResponseCode("900910");
                    redeemResponseMatchWso2DTO.setResponseMessage("Can not access the required resource with the provided access token. KeyType missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. KeyType missing.");
                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(redeemResponseMatchWso2DTO);
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    return redeemResponseMatchWso2DTO;
                }
            } else{
                redeemResponseMatchWso2DTO.setResponseCode("900401");
                redeemResponseMatchWso2DTO.setResponseMessage("Invalid credential");
                LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(redeemResponseMatchWso2DTO);
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                return redeemResponseMatchWso2DTO;
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

        if (null == credentialLookupDTO) {
            redeemResponseDTO.setResponseCode("101404");
            redeemResponseDTO.setResponseMessage("Unable to process your request. Credential details not found.");
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(redeemResponseMatchWso2DTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.warn("Credential details not found: " + redeemRequestDTO.getAccountNumber());
        } else if(!credentialLookupDTO.isActive()){
            redeemResponseDTO.setResponseCode("101403");
            redeemResponseDTO.setResponseMessage("Unable to process your request. Credential details not found.");
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(redeemResponseMatchWso2DTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.warn("Credential details not found: " + redeemRequestDTO.getAccountNumber());
        }else {

            try {
                RedeemRequestMdl redeemRequestMdl = RedeemRequestMapper.INSTANCE.toMdl(redeemRequestDTO);

                AcquirerMdl acquirer = new AcquirerMdl();
                redeemRequestMdl.setAcquirer(acquirer);

                ObjectMapper mapper = new ObjectMapper();
                try{
                    List<AcquirerDTO> acquirerDTOList = mapper.convertValue(redeemRequestDTO.getAcquirerReference(), new TypeReference<List<AcquirerDTO>>() { });
                    if(null != acquirerDTOList){
                        redeemRequestMdl.getAcquirer().setReference(acquirerDTOList.get(0).getValue());
                    }
                }catch (Exception e){
                    try {
                        AcquirerDTO acquirerDTO = mapper.convertValue(redeemRequestDTO.getAcquirerReference(), AcquirerDTO.class);
                        if (null != acquirerDTO) {
                            redeemRequestMdl.getAcquirer().setReference(acquirerDTO.getValue());
                        }
                    }catch (Exception ex){
                        LOGGER.warn("acquirer conversion on redeem: " + ex.getMessage());
                    }
                }

                if(null != redeemRequestDTO.getCustomerContact() && "sms".equalsIgnoreCase(redeemRequestDTO.getCustomerContact().getMechanism())) {
                    redeemRequestMdl.setChangeSmsMsisdn(redeemRequestDTO.getCustomerContact().getAddress());
                }
                redeemRequestMdl.getAcquirer().setId(credentialLookupDTO.getAcquirerId());
                redeemRequestMdl.getAcquirer().setPassword(credentialLookupDTO.getAcquirerPassword());
                redeemRequestMdl.getUser().setAuthUserName(credentialLookupDTO.getAuthUserName());
                redeemRequestMdl.getUser().setAuthPassWord(credentialLookupDTO.getAuthPassword());

                HttpEntity<RedeemRequestMdl> purchaseRequestEntity = new HttpEntity<RedeemRequestMdl>(redeemRequestMdl, httpHeaders);

                loggingPayloadMdl.setRequestOutPayload(redeemRequestMdl);
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                RedeemResponseMdl redeemResponseMdl = redeemWebService.redeemRestCall(purchaseRequestEntity, jwtExtractor.getKeyType(), loggingPayloadMdl);
                loggingPayloadMdl.setResponseInPayload(redeemResponseMdl);
                loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

                redeemResponseMdl.setSequenceNumber(redeemRequestMdl.getSequenceNumber());

                redeemResponseDTO = RedeemResponseMapper.INSTANCE.toDTO(redeemResponseMdl);
                redeemResponseDTO.setTransactionDate(FlashDates.currentDateTimeFlashFormatInString());
                redeemResponseDTO.setAcquirerReference(redeemRequestDTO.getAcquirerReference());

                redeemResponseDTO.setAcquirerReference(redeemRequestDTO.getAcquirerReference());

                redeemResponseMatchWso2DTO = RedeemResponseMapper.INSTANCE.toWso2DTO(redeemResponseDTO);
                redeemResponseMatchWso2DTO.getAmount().setCurrency("ZAR");

                if(null != redeemResponseMatchWso2DTO.getChangeVoucher() && null != redeemResponseMatchWso2DTO.getChangeVoucher().getAmount()){
                    redeemResponseMatchWso2DTO.getChangeVoucher().getAmount().setCurrency("ZAR");
                }

                loggingPayloadMdl.setResponseOutPayload(redeemResponseMatchWso2DTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            }catch (Exception e){
                LOGGER.error("redeemSvr Exception: " + e.getMessage());
                redeemResponseDTO.setResponseCode("101403");
                redeemResponseDTO.setResponseMessage("Unable to process your request at this moment.");
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(redeemResponseDTO);
            }
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));

        }
        return redeemResponseMatchWso2DTO;
    }
}
