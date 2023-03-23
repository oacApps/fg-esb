package co.za.flash.esb.oneforyou.trader.service;

import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.trader.dblookup.CredentialLookupDTO;
import co.za.flash.esb.oneforyou.trader.dblookup.LookUpService;
import co.za.flash.esb.oneforyou.trader.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.oneforyou.trader.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.trader.mapper.PurchaseRequestMapper;
import co.za.flash.esb.oneforyou.trader.mapper.PurchaseResponseMapper;
import co.za.flash.esb.oneforyou.trader.model.request.PurchaseRequestMdl;
import co.za.flash.esb.oneforyou.trader.model.response.PurchaseResponseMdl;
import co.za.flash.esb.oneforyou.trader.model.shared.AcquirerMdl;
import co.za.flash.esb.oneforyou.trader.restservice.PurchaseWebService;
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
public class PurchaseService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LookUpService lookUpService;

    private JwtExtractor jwtExtractor = new JwtExtractor();
    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

    @Autowired
    PurchaseWebService purchaseWebService;

    public PurchaseResponseDTO purchaseSvr(Map<String, String> headers, PurchaseRequestDTO purchaseRequest){
        LOGGER.debug("purchaseRequest: " + JsonStringMapper.toJsonString(purchaseRequest));
        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();

        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
        loggingPayloadMdl.setRequestInPayload(purchaseRequest);

        PurchaseResponseDTO purchaseResponseDTO = new PurchaseResponseDTO();

       try {
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                jwtExtractor.extract(headers);
                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    purchaseResponseDTO.setResponseCode("900910");
                    purchaseResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");

                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);

                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));

                    return purchaseResponseDTO;
                }
                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    purchaseResponseDTO.setResponseCode("900910");
                    purchaseResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. KeyType missing.");

                    loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);

                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. KeyType missing.");
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));

                    return purchaseResponseDTO;
                }
            } else{
                purchaseResponseDTO.setResponseCode("900401");
                purchaseResponseDTO.setResponseMessage("Invalid credential");

                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);

                LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                return purchaseResponseDTO;
            }

        }catch (JSONException e) {
           LOGGER.error("jwtExtractor JSONException: " + e.getLocalizedMessage());
           LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        } catch (JWTException e) {
           LOGGER.error("jwtExtractor JWTException: " + e.getLocalizedMessage());
           LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }

        loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
        if(null != jwtExtractor.getKeyType()) {
            loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        CredentialLookupDTO credentialLookupDTO=null;
        try {
            credentialLookupDTO = lookUpService.getByOrganizationAndKeyType(jwtExtractor.getOrganization(), jwtExtractor.getKeyType());
        }catch (RuntimeException e){
            LOGGER.warn("DB LookUpService  RuntimeException: " + e.getLocalizedMessage());
        }

        if (null == credentialLookupDTO) {
            purchaseResponseDTO.setResponseCode("101404");
            purchaseResponseDTO.setResponseMessage("Unable to process your request. Credential details not found.");

            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);

            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.warn("Credential details not found: " + purchaseRequest.getAccountNumber());
        } else if(!credentialLookupDTO.isActive()){
            purchaseResponseDTO.setResponseCode("101403");
            purchaseResponseDTO.setResponseMessage("Unable to process your request. Credential details not found.");

            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);

            LOGGER.warn("Credential details not found: " + purchaseRequest.getAccountNumber());
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }else {
            try {
                PurchaseRequestMdl purchaseRequestMdl = PurchaseRequestMapper.INSTANCE.toMdl(purchaseRequest);
                AcquirerMdl acquirer = new AcquirerMdl();
                purchaseRequestMdl.setAcquirer(acquirer);

                ObjectMapper mapper = new ObjectMapper();
                try{
                    List<AcquirerDTO> acquirerDTOList = mapper.convertValue(purchaseRequest.getAcquirerReference(), new TypeReference<List<AcquirerDTO>>() { });
                    if(null != acquirerDTOList){
                        purchaseRequestMdl.getAcquirer().setReference(acquirerDTOList.get(0).getValue());
                    }
                }catch (Exception e){
                    try {
                        AcquirerDTO acquirerDTO = mapper.convertValue(purchaseRequest.getAcquirerReference(), AcquirerDTO.class);
                        if (null != acquirerDTO) {
                            purchaseRequestMdl.getAcquirer().setReference(acquirerDTO.getValue());
                        }
                    }catch (Exception ex){
                        LOGGER.warn("acquirer conversion on purchase: " + ex.getMessage());
                    }
                }

                purchaseRequestMdl.getAcquirer().setId(credentialLookupDTO.getAcquirerId());
                purchaseRequestMdl.getAcquirer().setPassword(credentialLookupDTO.getAcquirerPassword());
                purchaseRequestMdl.getUser().setAuthUserName(credentialLookupDTO.getAuthUserName());
                purchaseRequestMdl.getUser().setAuthPassWord(credentialLookupDTO.getAuthPassword());

                HttpEntity<PurchaseRequestMdl> purchaseRequestEntity = new HttpEntity<PurchaseRequestMdl>(purchaseRequestMdl, httpHeaders);

                loggingPayloadMdl.setRequestOutPayload(purchaseRequestMdl);
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                PurchaseResponseMdl purchaseResponseMdl = purchaseWebService.purchaseRestCall(purchaseRequestEntity, jwtExtractor.getKeyType(), loggingPayloadMdl);

                loggingPayloadMdl.setResponseInPayload(purchaseResponseMdl);
                loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

                purchaseResponseMdl.setSequenceNumber(purchaseRequestMdl.getSequenceNumber());

                purchaseResponseDTO = PurchaseResponseMapper.INSTANCE.toDTO(purchaseResponseMdl);
                if (purchaseResponseDTO.getResponseCode().equals("0000")) {
                    purchaseResponseDTO.setResponseMessage("Success");
                }
                purchaseResponseDTO.setTransactionDate(FlashDates.currentDateTimeFlashFormatInString());
                purchaseResponseDTO.setAcquirerReference(purchaseRequest.getAcquirerReference());

                loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            }catch (Exception e){
                LOGGER.error("purchaseSvr Exception: " + e.getMessage());
                purchaseResponseDTO.setResponseCode("101403");
                purchaseResponseDTO.setResponseMessage("Unable to process your request at this moment.");
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);
            }
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }

        return purchaseResponseDTO;
    }
}
