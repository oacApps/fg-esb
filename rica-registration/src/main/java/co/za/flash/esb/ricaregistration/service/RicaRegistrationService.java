package co.za.flash.esb.ricaregistration.service;

import co.za.flash.esb.library.BuildHeader;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.ricaregistration.dto.request.RegistrationsRequestDTO;
import co.za.flash.esb.ricaregistration.dto.response.RegistrationsResponseDTO;
import co.za.flash.esb.ricaregistration.dto.response.SimCardInfoResponseDTO;
import co.za.flash.esb.ricaregistration.mapper.RegistrationRequestMapper;
import co.za.flash.esb.ricaregistration.model.request.RegistrationsRequestMdl;
import co.za.flash.esb.ricaregistration.restservice.RegistrationWebService;
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

import java.text.ParseException;
import java.util.Map;

@Service
public class RicaRegistrationService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RegistrationWebService webService;

    @Value("${flash.rica.registrations.production}")
    private String ricaRegistrationsProduction;

    @Value("${flash.rica.registrations.sandbox}")
    private String ricaRegistrationsSandbox;

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();
    BuildHeader buildHeader = new BuildHeader();

    public Object registration(Map<String, String> headers, RegistrationsRequestDTO requestDTO){

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("Rica Registration");
        loggingPayloadMdl.setRequestInPayload(requestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        String registrationURL = null;
        ResponseEntity<Object> response = null;

        try {
            JwtExtractor jwtExtractor = new JwtExtractor();
            RegistrationsRequestMdl registrationsRequestMdl = new RegistrationsRequestMdl();

            if (headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                try {
                    jwtExtractor.extract(headers);
                    String environment = jwtExtractor.getKeyType();
                    registrationURL = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(environment) ? ricaRegistrationsProduction : ricaRegistrationsSandbox;

                    registrationsRequestMdl = RegistrationRequestMapper.INSTANCE.toRequestMDL(requestDTO);

                } catch (JSONException | JWTException | ParseException e) {
                    LOGGER.error("Rica Registration Exception: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }

                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    response = new ResponseEntity<>("Can not access the required resource with the provided access token. Organization name missing.", HttpStatus.BAD_REQUEST);
                    loggingPayloadMdl.setResponseOutPayload(response);
                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    return response;
                }
                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    response = new ResponseEntity<>("Can not access the required resource with the provided access token. Key type missing.", HttpStatus.BAD_REQUEST);
                    loggingPayloadMdl.setResponseOutPayload(response);
                    loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                    return response;
                }

            }

            loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
            loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());


            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);

            HttpEntity<RegistrationsRequestMdl> entity = new HttpEntity<>(registrationsRequestMdl, httpHeaders);

            loggingPayloadMdl.setRequestOutPayload(registrationsRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setApiResource(registrationURL);

            String bkResponse = webService.registrationRestCall(entity, registrationURL);

            RegistrationsResponseDTO registrationsResponseDTO = new RegistrationsResponseDTO();

            try{
                Gson gson = new Gson();
                registrationsResponseDTO = gson.fromJson(bkResponse, RegistrationsResponseDTO.class);
                response = new ResponseEntity<>(registrationsResponseDTO, HttpStatus.OK);
            } catch (Exception e){
                LOGGER.error("gson exception: " + e.getMessage());
                e.printStackTrace();
            }

            loggingPayloadMdl.setResponseInPayload(registrationsResponseDTO);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            loggingPayloadMdl.setResponseOutPayload(response);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        }catch (Exception e){
            LOGGER.error("Rica registration exception: ");
            e.printStackTrace();
        }finally {
            LOGGER.info("payload: "+ JsonStringMapper.toJsonString(loggingPayloadMdl));
        }

        if (null == response) {
            response = new ResponseEntity<>("Error processing your request. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
            loggingPayloadMdl.setResponseOutPayload(response);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            return response;
        }
        return response;
    }
}
