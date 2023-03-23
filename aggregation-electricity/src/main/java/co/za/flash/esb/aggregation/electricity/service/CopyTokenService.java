package co.za.flash.esb.aggregation.electricity.service;

import co.za.flash.esb.aggregation.electricity.dto.CopyTokenRequestDTO;
import co.za.flash.esb.aggregation.electricity.error.Wso2Error;
import co.za.flash.esb.aggregation.electricity.mapper.CopTokenRequestMapper;
import co.za.flash.esb.aggregation.electricity.mapper.OneBalanceResponseMapper;
import co.za.flash.esb.aggregation.electricity.model.request.ElectricityRequestMdl;
import co.za.flash.esb.aggregation.electricity.model.response.CopyTokenResponseMdl;
import co.za.flash.esb.aggregation.electricity.model.response.OBElecResponse;
import co.za.flash.esb.aggregation.electricity.model.response.OneBalance;
import co.za.flash.esb.aggregation.electricity.model.response.OneBalanceRefLong;
import co.za.flash.esb.aggregation.electricity.restclient.CopyTokenWebService;
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
public class CopyTokenService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${flash.debug}")
    private boolean isDebug;

    Wso2Error wso2Error = new Wso2Error();

    BuildHeader buildHeader = new BuildHeader();

    @Autowired
    CopyTokenWebService webService;

    public Object service(Map<String, String> headers, CopyTokenRequestDTO copyTokenRequestDTO, boolean isTest) {

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("electricity-lookup");
        loggingPayloadMdl.setRequestInPayload(copyTokenRequestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        JwtExtractor jwtExtractor = new JwtExtractor();
        Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

        CopyTokenResponseMdl responseMdl = new CopyTokenResponseMdl();

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
                    loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());
                    loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
                } catch (JSONException | JWTException e) {
                    e.printStackTrace();
                    LOGGER.error("CopyToken Service jwtExtractor JWTException: " + e.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("CopyToken Service wso2JwtKeys: " + e.getLocalizedMessage());
                }

                HttpHeaders httpHeaders = buildHeader.build(headers);
                // rest of the code
                ElectricityRequestMdl electricityRequestMdl = CopTokenRequestMapper.INSTANCE.toMdl(copyTokenRequestDTO);
                electricityRequestMdl.setAmount(0L);
                long testSandBox = isTest ? 1L : 0L;
                electricityRequestMdl.setTrans_route_type(EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(jwtExtractor.getKeyType()) ? 0L : testSandBox );

                loggingPayloadMdl.setRequestOutPayload(electricityRequestMdl);
                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

                HttpEntity<ElectricityRequestMdl> entity = new HttpEntity<>(electricityRequestMdl, httpHeaders);

                if(isDebug) LOGGER.debug("Entity sent to backend: " + JsonStringMapper.toJsonString(entity));
                String bkResponse = webService.backendService(entity, jwtExtractor.getKeyType(), loggingPayloadMdl);
                LOGGER.info("bkResponse: " + bkResponse);

                try{
                    Gson gson = new Gson();
                    responseMdl = gson.fromJson(bkResponse, CopyTokenResponseMdl.class);

                    if(null == responseMdl.getOBElecResponse()){
                        OneBalance oneBalance = gson.fromJson(bkResponse, OneBalance.class);
                        /*OBElecResponse obElecResponse = OneBalanceResponseMapper.INSTANCE.toOBElecResponse(oneBalance.getOneBalance());
                        responseMdl.setOBElecResponse(obElecResponse);*/
                        responseMdl.setOneBalance(oneBalance.getOneBalance());

                        loggingPayloadMdl.setResponseInPayload(OneBalanceResponseMapper.INSTANCE.toOneBalanceSM(oneBalance));
                        loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    }else{
                        loggingPayloadMdl.setResponseInPayload(responseMdl);
                        loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());
                    }

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
            LOGGER.error("CopyToken Service : " + e.getLocalizedMessage());
            return new ResponseEntity("An internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info("Payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }

        return responseMdl;
    }
}
