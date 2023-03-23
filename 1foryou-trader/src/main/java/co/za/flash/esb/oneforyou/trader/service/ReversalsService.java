package co.za.flash.esb.oneforyou.trader.service;

import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.dblookup.CredentialLookupDTO;
import co.za.flash.esb.oneforyou.trader.dto.request.ReversalsRedemptionRequestDTO;
import co.za.flash.esb.oneforyou.trader.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.trader.mapper.ReversalsResponseMapper;
import co.za.flash.esb.oneforyou.trader.model.shared.AcquirerMdl;
import co.za.flash.esb.oneforyou.trader.restservice.ReversalsWebService;
import co.za.flash.esb.oneforyou.trader.dblookup.LookUpService;
import co.za.flash.esb.oneforyou.trader.dto.response.ReversalsRedemptionResponseDTO;
import co.za.flash.esb.oneforyou.trader.mapper.ReversalsRequestMapper;
import co.za.flash.esb.oneforyou.trader.model.request.ReversalsRedemptionRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.ReversalsResponseMdl;
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
public class ReversalsService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LookUpService lookUpService;

    private final JwtExtractor jwtExtractor = new JwtExtractor();
    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();


    @Autowired
    ReversalsWebService reversalsWebService;

    public ReversalsRedemptionResponseDTO reversalsSvr(Map<String, String> headers, ReversalsRedemptionRequestDTO reversalsRedemptionRequestDTO){

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();

        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
        loggingPayloadMdl.setRequestInPayload(reversalsRedemptionRequestDTO);

        ReversalsRedemptionResponseDTO reversalsRedemptionResponseDTO = new ReversalsRedemptionResponseDTO();

        try {
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                jwtExtractor.extract(headers);
                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    reversalsRedemptionResponseDTO.setResponseCode("900910");
                    reversalsRedemptionResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(reversalsRedemptionResponseDTO);
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    return reversalsRedemptionResponseDTO;
                }
                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    reversalsRedemptionResponseDTO.setResponseCode("900910");
                    reversalsRedemptionResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. KeyType missing.");
                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(reversalsRedemptionResponseDTO);
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. KeyType missing.");
                    return reversalsRedemptionResponseDTO;
                }
            } else{
                reversalsRedemptionResponseDTO.setResponseCode("900401");
                reversalsRedemptionResponseDTO.setResponseMessage("Invalid credential");
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(reversalsRedemptionResponseDTO);
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
                return reversalsRedemptionResponseDTO;
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
            reversalsRedemptionResponseDTO.setResponseCode("101404");
            reversalsRedemptionResponseDTO.setResponseMessage("Unable to process your request. Credential details not found.");
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(reversalsRedemptionResponseDTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.warn("Credential details not found: " + reversalsRedemptionRequestDTO.getAccountNumber());
        } else if(!credentialLookupDTO.isActive()){
            reversalsRedemptionResponseDTO.setResponseCode("101403");
            reversalsRedemptionResponseDTO.setResponseMessage("Unable to process your request. Credential details not found.");
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(reversalsRedemptionResponseDTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.warn("Credential details not found: " + reversalsRedemptionRequestDTO.getAccountNumber());
        }else {
            try {
                ReversalsRedemptionRequestMdl reversalsRedemptionRequestMdl = ReversalsRequestMapper.INSTANCE.toMdl(reversalsRedemptionRequestDTO);

                AcquirerMdl acquirer = new AcquirerMdl();
                reversalsRedemptionRequestMdl.setAcquirer(acquirer);

                ObjectMapper mapper = new ObjectMapper();
                try{
                    List<AcquirerDTO> acquirerDTOList = mapper.convertValue(reversalsRedemptionRequestDTO.getAcquirerReference(), new TypeReference<List<AcquirerDTO>>() { });
                    if(null != acquirerDTOList){
                        reversalsRedemptionRequestMdl.getAcquirer().setReference(acquirerDTOList.get(0).getValue());
                    }
                }catch (Exception e){
                    try {
                        AcquirerDTO acquirerDTO = mapper.convertValue(reversalsRedemptionRequestDTO.getAcquirerReference(), AcquirerDTO.class);
                        if (null != acquirerDTO) {
                            reversalsRedemptionRequestMdl.getAcquirer().setReference(acquirerDTO.getValue());
                        }
                    }catch (Exception ex){
                        LOGGER.warn("acquirer conversion on reversal: " + ex.getMessage());
                    }
                }

                reversalsRedemptionRequestMdl.getAcquirer().setId(credentialLookupDTO.getAcquirerId());
                reversalsRedemptionRequestMdl.getAcquirer().setPassword(credentialLookupDTO.getAcquirerPassword());
                reversalsRedemptionRequestMdl.getUser().setAuthUserName(credentialLookupDTO.getAuthUserName());
                reversalsRedemptionRequestMdl.getUser().setAuthPassWord(credentialLookupDTO.getAuthPassword());

                HttpEntity<ReversalsRedemptionRequestMdl> reversalsRequestEntity = new HttpEntity<>(reversalsRedemptionRequestMdl, httpHeaders);


                loggingPayloadMdl.setRequestOutPayload(reversalsRedemptionRequestMdl);
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                ReversalsResponseMdl reversalsResponseMdl = reversalsWebService.reversaleRestCall(reversalsRequestEntity, jwtExtractor.getKeyType(), loggingPayloadMdl);
                loggingPayloadMdl.setResponseInPayload(reversalsResponseMdl);
                loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

                reversalsResponseMdl.setSequenceNumber(reversalsRedemptionRequestMdl.getSequenceNumber());

                reversalsRedemptionResponseDTO = ReversalsResponseMapper.INSTANCE.toDTO(reversalsResponseMdl);
                reversalsRedemptionResponseDTO.setTransactionDate(FlashDates.currentDateTimeFlashFormatInString());
                reversalsRedemptionResponseDTO.setAcquirerReference(reversalsRedemptionRequestDTO.getAcquirerReference());

                loggingPayloadMdl.setResponseOutPayload(reversalsRedemptionResponseDTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            }catch (Exception e){
                LOGGER.info("reversalsSvr: " + e.getMessage());
                reversalsRedemptionResponseDTO.setResponseCode("101403");
                reversalsRedemptionResponseDTO.setResponseMessage("Unable to process your request at this moment.");
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(reversalsRedemptionResponseDTO);
            }
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return reversalsRedemptionResponseDTO;
    }
}
