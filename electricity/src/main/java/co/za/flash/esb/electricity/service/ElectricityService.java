package co.za.flash.esb.electricity.service;

import co.za.flash.esb.electricity.dto.request.LookupRequestDTO;
import co.za.flash.esb.electricity.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.electricity.dto.response.LookupResponseDTO;
import co.za.flash.esb.electricity.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.electricity.mapper.LookupRequestMapper;
import co.za.flash.esb.electricity.mapper.LookupResponseMapper;
import co.za.flash.esb.electricity.mapper.PurchaseRequestMapper;
import co.za.flash.esb.electricity.mapper.PurchaseResponseMapper;
import co.za.flash.esb.electricity.model.request.LookupRequestMdl;
import co.za.flash.esb.electricity.model.request.PurchaseRequestMdl;
import co.za.flash.esb.electricity.model.response.LookupResponseMdl;
import co.za.flash.esb.electricity.model.response.PurchaseResponseMdl;
import co.za.flash.esb.electricity.restservice.ConsumeWebService;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.DBLoggingPayload;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ElectricityService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConsumeWebService webService;

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

    //taking DTOs and converting into the model and adding headers
    public LookupResponseDTO lookupService(Map<String, String> headers, LookupRequestDTO lookupRequestDTO){
        JwtExtractor jwtExtractor =new JwtExtractor();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        LookupResponseDTO lookupResponseDTO = new LookupResponseDTO();
        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("electricity-lookup");
        loggingPayloadMdl.setRequestInPayload(lookupRequestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        try {
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {

                jwtExtractor.extract(headers);

                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    lookupResponseDTO.setResponseCode(900910);
                    lookupResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");

                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setResponseOutPayload(lookupResponseDTO);

                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.info("payload: " + toJsonString(loggingPayloadMdl));

                    return lookupResponseDTO;
                }
                loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());

                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    lookupResponseDTO.setResponseCode(900910);
                    lookupResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. KeyType missing.");

                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setResponseOutPayload(lookupResponseDTO);

                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. KeyType missing.");
                    LOGGER.info("payload: " + toJsonString(loggingPayloadMdl));

                    return lookupResponseDTO;
                }

                loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());

            } else {
                lookupResponseDTO.setResponseCode(101515);
                lookupResponseDTO.setResponseMessage("x-jwt-assertion is missing from header");
                LOGGER.error("Response processing failed (101515): x-jwt-assertion is missing from header");
                loggingPayloadMdl.setResponseOutPayload(lookupResponseDTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                LOGGER.info("payload: "+ toJsonString(loggingPayloadMdl));
                return lookupResponseDTO;
            }

            LookupRequestMdl lookupRequestMdl = LookupRequestMapper.INSTANCE.toMdl(lookupRequestDTO);

            loggingPayloadMdl.setRequestOutPayload(lookupRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            HttpEntity<LookupRequestMdl> entity = new HttpEntity<LookupRequestMdl>(lookupRequestMdl, httpHeaders);

            LookupResponseMdl lookupResponseMdl = webService.lookupService(entity, jwtExtractor.getKeyType(), loggingPayloadMdl);

            loggingPayloadMdl.setResponseInPayload(lookupResponseMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            lookupResponseDTO = LookupResponseMapper.INSTANCE.toDTO(lookupResponseMdl);

            if (lookupResponseMdl.getResponseCode() == 0) {
                lookupResponseDTO.setAcquirer(lookupRequestDTO.getAcquirer());
            }
            // What we send to API Manager
            loggingPayloadMdl.setResponseOutPayload(lookupResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("lookupService Exception" +  e.getLocalizedMessage());
        }finally {
            LOGGER.info("lookupService payload: "+ toJsonString(loggingPayloadMdl));
        }
        return lookupResponseDTO;
    }

    public PurchaseResponseDTO purchaseRequestService(Map<String, String> headers, PurchaseRequestDTO purchaseRequestDTO){
        JwtExtractor jwtExtractor =new JwtExtractor();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        PurchaseResponseDTO purchaseResponseDTO = new PurchaseResponseDTO();

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("electricity-purchase");
        loggingPayloadMdl.setApiKey("electricity-purchase");
        loggingPayloadMdl.setRequestInPayload(purchaseRequestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
        try {
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                jwtExtractor.extract(headers);
                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    purchaseResponseDTO.setResponseCode(900910);
                    purchaseResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");

                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);

                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.info("purchaseRequestService payload: " + toJsonString(loggingPayloadMdl));

                    return purchaseResponseDTO;
                }
                loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());

                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    purchaseResponseDTO.setResponseCode(900910);
                    purchaseResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. KeyType missing.");

                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);

                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. KeyType missing.");
                    LOGGER.info("purchaseRequestService payload: " + toJsonString(loggingPayloadMdl));

                    return purchaseResponseDTO;
                }
                loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());

            } else {
                purchaseResponseDTO.setResponseCode(101515);
                purchaseResponseDTO.setResponseMessage("x-jwt-assertion is missing from header");
                LOGGER.error("Response processing failed (101515): x-jwt-assertion is missing from header");

                loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                LOGGER.info("purchaseRequestService payload: "+ toJsonString(loggingPayloadMdl));
                return purchaseResponseDTO;
            }

            PurchaseRequestMdl purchaseRequestMdl = PurchaseRequestMapper.INSTANCE.toMdl(purchaseRequestDTO);
            // What we Send to Back end
            loggingPayloadMdl.setRequestOutPayload(purchaseRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            HttpEntity<PurchaseRequestMdl> entity = new HttpEntity<PurchaseRequestMdl>(purchaseRequestMdl, httpHeaders);

            PurchaseResponseMdl purchaseResponseMdl = webService.purchaseRequestService(entity, jwtExtractor.getKeyType(), loggingPayloadMdl);

             // What we Receive from Back end
            loggingPayloadMdl.setResponseInPayload(purchaseResponseMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            purchaseResponseDTO = PurchaseResponseMapper.INSTANCE.toDTO(purchaseResponseMdl);

            if (purchaseResponseMdl.getResponseCode() == 0) {
                purchaseResponseDTO.setAcquirer(purchaseRequestDTO.getAcquirer());
            }
            // What we send to API Manager
            loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("purchase Exception" +  e.getLocalizedMessage());
        }finally {
            LOGGER.info("purchaseRequestService payload: "+ toJsonString(loggingPayloadMdl));
        }
        return purchaseResponseDTO;
    }
    private String toJsonString(Object object){
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LOGGER.error("toJsonString Exception" +  e.getLocalizedMessage());
        }

        return json;
    }
}
