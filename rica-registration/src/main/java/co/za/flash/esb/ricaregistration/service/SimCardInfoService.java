package co.za.flash.esb.ricaregistration.service;

import co.za.flash.esb.library.BuildHeader;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.ricaregistration.dto.request.AgentRicaStatusRequestDTO;
import co.za.flash.esb.ricaregistration.dto.request.SimCardInfoRequestDTO;
import co.za.flash.esb.ricaregistration.dto.response.SimCardInfoResponseDTO;
import co.za.flash.esb.ricaregistration.mapper.SimCardInfoMapper;
import co.za.flash.esb.ricaregistration.model.request.SimCardInfoRequestMdl;
import co.za.flash.esb.ricaregistration.restservice.AgentStatusWebService;
import co.za.flash.esb.ricaregistration.restservice.SimCardInfoWebService;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class SimCardInfoService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimCardInfoWebService webService;

    @Value("${flash.rica.sim.info.production}")
    private String simCardInfoProduction;

    @Value("${flash.rica.sim.info.sandbox}")
    private String simCardInfoSandbox;

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();
    BuildHeader buildHeader = new BuildHeader();

    public Object simCardInfo(Map<String, String> headers, SimCardInfoRequestDTO requestDTO){

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("Agent Rica Status");
        loggingPayloadMdl.setRequestInPayload(requestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        JwtExtractor jwtExtractor = new JwtExtractor();

        String simCardInfoURL = null;
        ResponseEntity<Object> response = null;
        if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
            try {
                jwtExtractor.extract(headers);
                String environment = jwtExtractor.getKeyType();
                simCardInfoURL = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(environment) ? simCardInfoProduction : simCardInfoSandbox;
                
            } catch (JSONException | JWTException e) {
                LOGGER.error("Agent Rica Status jwtExtractor: " + e.getLocalizedMessage());
                e.printStackTrace();
            }

            if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                response = new ResponseEntity<>("Can not access the required resource with the provided access token. Organization name missing.", HttpStatus.BAD_REQUEST);
                loggingPayloadMdl.setResponseOutPayload(response);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                LOGGER.info("payload: "+JsonStringMapper.toJsonString(loggingPayloadMdl));
                return response;
            }
            if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                response = new ResponseEntity<>("Can not access the required resource with the provided access token. Key type missing.", HttpStatus.BAD_REQUEST);
                loggingPayloadMdl.setResponseOutPayload(response);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                LOGGER.info("payload: "+JsonStringMapper.toJsonString(loggingPayloadMdl));
                return response.getBody();
            }
            
        }

        loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
        loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        SimCardInfoRequestMdl requestMdl = SimCardInfoMapper.INSTANCE.toRequestMDL(requestDTO);
        HttpEntity<SimCardInfoRequestMdl> entity = new HttpEntity<SimCardInfoRequestMdl>(requestMdl, httpHeaders);

        loggingPayloadMdl.setRequestOutPayload(requestMdl);
        loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        String bkResponse = webService.backendService(entity, simCardInfoURL);
        SimCardInfoResponseDTO simCardInfoResponseDTO=new SimCardInfoResponseDTO();
        try{
            Gson gson = new Gson();
            simCardInfoResponseDTO = gson.fromJson(bkResponse, SimCardInfoResponseDTO.class);
            response = new ResponseEntity<>(simCardInfoResponseDTO, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("gson exception: " + e.getMessage());
            e.printStackTrace();
        }
        loggingPayloadMdl.setApiResource(simCardInfoURL);

        loggingPayloadMdl.setResponseInPayload(bkResponse);
        loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        if(null == response){
            response = new ResponseEntity<>("Error processing your request. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
            loggingPayloadMdl.setResponseOutPayload(response);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            LOGGER.info("payload: "+JsonStringMapper.toJsonString(loggingPayloadMdl));
            return response.getBody();
        }

        loggingPayloadMdl.setResponseOutPayload(response);
        loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        LOGGER.info("payload: "+ JsonStringMapper.toJsonString(loggingPayloadMdl));

        return response.getBody();
    }


}
