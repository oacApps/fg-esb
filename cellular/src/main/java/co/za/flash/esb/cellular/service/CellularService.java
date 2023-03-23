package co.za.flash.esb.cellular.service;

import co.za.flash.esb.cellular.data.RequiredRole;
import co.za.flash.esb.cellular.database.CredentialLookup;
import co.za.flash.esb.cellular.dto.CellularPinLessResponseDTO;
import co.za.flash.esb.cellular.dto.CellularRequestDTO;
import co.za.flash.esb.cellular.dto.Wso2ErrorMatchDTO;
import co.za.flash.esb.cellular.mapper.CellularRequestMapper;
import co.za.flash.esb.cellular.mapper.OBAirtimeResponseMapper;
import co.za.flash.esb.cellular.mapper.OneBalanceResponseMapper;
import co.za.flash.esb.cellular.model.*;
import co.za.flash.esb.cellular.restservice.ConsumeWebService;
import co.za.flash.esb.library.BuildHeader;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.enums.cellular.CellularNetwork;
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
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Map;

@Service
public class CellularService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConsumeWebService webService;

    @Autowired
    CredentialLookupService credentialLookupService;

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

    BuildHeader buildHeader = new BuildHeader();


    @Autowired
    RequiredRole requiredRole;

    @Value("${flash.debug}")
    private boolean isDebug;

    /**
     *
     * @param headers
     * @param cellularRequestDTO
     * @return
     */
    public Object
    clientService(Map<String, String> headers, CellularRequestDTO cellularRequestDTO, boolean isPinLess){

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("Cellular-purchase");
        CellularPinLessResponseDTO cellularResponseDTO = new CellularPinLessResponseDTO();

        loggingPayloadMdl.setRequestInPayload(cellularRequestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        JwtExtractor jwtExtractor = new JwtExtractor();

        if (isDebug) LOGGER.debug("Client Request Headers: "+ headers.toString());

        if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
            try {
                jwtExtractor.extract(headers);
                // For testing Only

                    /*jwtExtractor.getRole().put("cellular-cellc-pinless-airtime");
                    jwtExtractor.getRole().put("cellular-mtn-pinless-airtime");
                jwtExtractor.getRole().put("cellular-mtn-pin-airtime");*/

                String pinOrLess = isPinLess ? "pinless" : "pin";
                String requestedRole = ("cellular-"+ CellularNetwork.fromString(cellularRequestDTO.getNetwork()).getCode()+"-"+pinOrLess+"-"+cellularRequestDTO.getType().getType()).toLowerCase();

                if(!requiredRole.validateAccess(requestedRole, jwtExtractor.getRole())){
                    Wso2ErrorMatchDTO wso2ErrorMatchDTO = requiredRole.wso2MatchError();
                    loggingPayloadMdl.setResponseOutPayload(wso2ErrorMatchDTO);
                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                    LOGGER.error("Role based Access: Customer doesn't have enough access; trying to access : " + requestedRole);
                    LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    return wso2ErrorMatchDTO;
                }
            } catch (JWTException | JSONException e) {
                LOGGER.error("Purchase jwtExtractor JWTException: " + e.getLocalizedMessage());
                LOGGER.info("Request in: " + JsonStringMapper.toJsonString(cellularRequestDTO));
                e.printStackTrace();
            } catch (Exception e) {
                LOGGER.error("Purchase jwtExtractor exception: " + e.getLocalizedMessage());
                LOGGER.info("Request in: " + JsonStringMapper.toJsonString(cellularRequestDTO));
                e.printStackTrace();
            }

            if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                cellularResponseDTO.setResponseCode(900910);
                cellularResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");

                loggingPayloadMdl.setResponseOutPayload(cellularResponseDTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                return cellularResponseDTO;
            }
            if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                cellularResponseDTO.setResponseCode(900910);
                cellularResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. Key type missing.");
                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Key type missing.");
                loggingPayloadMdl.setResponseOutPayload(cellularResponseDTO);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                return cellularResponseDTO;
            }
        }else {
            cellularResponseDTO.setResponseCode(900401);
            cellularResponseDTO.setResponseMessage("Invalid credential");
            LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
            loggingPayloadMdl.setResponseOutPayload(cellularResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            return cellularResponseDTO;
        }

        loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
        loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());

        Object obResponse=new Object();
        try {
            HttpHeaders httpHeaders = buildHeader.build(headers);
            /*if(headers.containsKey(HeadersKey.X_JWT_ASSERTION.getValue())){
                httpHeaders.set(HeadersKey.X_JWT_ASSERTION.getValue(), headers.get(HeadersKey.X_JWT_ASSERTION.getValue()));
            }
            if(headers.containsKey(HeadersKey.AUTHORIZATION.getValue())){
                httpHeaders.set(HeadersKey.AUTHORIZATION.getValue(), headers.get(HeadersKey.AUTHORIZATION.getValue()));
            }
            httpHeaders.set(HeadersKey.CONTENT_TYPE.getValue(), HeadersKey.MEDIA_TYPE_APPLICATION_JSON.getValue());
            httpHeaders.set(HeadersKey.ACCEPT.getValue(), HeadersKey.MEDIA_TYPE_APPLICATION_JSON.getValue());*/

            CellularRequestMdl cellularRequestMdl = CellularRequestMapper.INSTANCE.toMdl(cellularRequestDTO);
            cellularRequestMdl.setTrans_pin_type(isPinLess ? 0L : 1L);

            clientIdPassLookup(cellularRequestMdl, jwtExtractor.getOrganization(), jwtExtractor.getKeyType());

            // Set trans_route_type based on KeyType
            /*cellularRequestMdl.setTrans_route_type(jwtExtractor.getKeyType().equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? 1L : 0L);*/
            cellularRequestMdl.setTrans_route_type(EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(jwtExtractor.getKeyType()) ? 0L : 1L);

            loggingPayloadMdl.setRequestOutPayload(cellularRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            HttpEntity<CellularRequestMdl> entity = new HttpEntity<CellularRequestMdl>(cellularRequestMdl, httpHeaders);

            if(isDebug) LOGGER.debug("Entity sent to backend: " + JsonStringMapper.toJsonString(entity));

            String bkResponse = webService.backendService(entity, jwtExtractor.getKeyType(), loggingPayloadMdl);

            try{
                Gson gson = new Gson();
                if("TELKOM".equalsIgnoreCase(cellularRequestDTO.getNetwork())) {
                    OBAirtimeResponseTelkom obAirtimeResponse = gson.fromJson(bkResponse, OBAirtimeResponseTelkom.class);
                    if (null != obAirtimeResponse.getOBAirtimeResponse()) {
                        obResponse = obAirtimeResponse.getOBAirtimeResponse();
                        loggingPayloadMdl.setResponseInPayload(OBAirtimeResponseMapper.INSTANCE.toObAirtimeTelkomSm(obAirtimeResponse));
                    } else if (null == obAirtimeResponse.getOBAirtimeResponse()) {
                        OneBalance oneBalance = gson.fromJson(bkResponse, OneBalance.class);
                        obResponse = oneBalance.getOneBalance();
                        loggingPayloadMdl.setResponseInPayload(OneBalanceResponseMapper.INSTANCE.toOneBalanceSM(oneBalance));
                    }
                } else {
                    OBAirtimeResponse obAirtimeResponse = gson.fromJson(bkResponse, OBAirtimeResponse.class);
                    if (null != obAirtimeResponse.getOBAirtimeResponse()) {
                        obResponse = obAirtimeResponse.getOBAirtimeResponse();
                        loggingPayloadMdl.setResponseInPayload(OBAirtimeResponseMapper.INSTANCE.toObAirtimeSm(obAirtimeResponse));
                    } else if (null == obAirtimeResponse.getOBAirtimeResponse()) {
                        OneBalance oneBalance = gson.fromJson(bkResponse, OneBalance.class);
                        obResponse = oneBalance.getOneBalance();
                        loggingPayloadMdl.setResponseInPayload(OneBalanceResponseMapper.INSTANCE.toOneBalanceSM(oneBalance));
                    }
                }
            } catch (Exception e){
             LOGGER.error("gson exception: " + e.getMessage());
             e.printStackTrace();
            }

            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            loggingPayloadMdl.setResponseOutPayload(obResponse);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        } catch (NullPointerException | ParseException e){
            LOGGER.error("clientService NullPointerException/RuntimeException: " + e.getMessage());
            e.printStackTrace();
            cellularResponseDTO.setResponseCode(900500);
            cellularResponseDTO.setResponseMessage("Request processing failed");

            loggingPayloadMdl.setResponseOutPayload(cellularResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            return cellularResponseDTO;
        } finally {
            LOGGER.info("payload: "+JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return obResponse;
    }

    /**
     *
     * @param cellularRequestMdl
     */
    private void clientIdPassLookup(CellularRequestMdl cellularRequestMdl, String organization, String keyType) {
        try {
            CredentialLookup credentialLookup = credentialLookupService.credentialLookup(organization, keyType);
            if(null !=credentialLookup) {
                cellularRequestMdl.setClient_id(credentialLookup.getClientId());
                cellularRequestMdl.setPassword(credentialLookup.getPassword());
            }else{
                throw new NullPointerException("CredentialLookup: No records found for organization name: " + organization + " and keyType: " + keyType);
            }
        }catch (NullPointerException e){
            LOGGER.error("clientIdPassLookup: " + e.getMessage());
            e.printStackTrace();
            throw new NullPointerException("CredentialLookup: No records found for organization name: " + organization + " and keyType: " + keyType);
        }
    }

}
