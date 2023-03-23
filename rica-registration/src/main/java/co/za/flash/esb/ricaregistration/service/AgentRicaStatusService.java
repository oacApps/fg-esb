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
import co.za.flash.esb.ricaregistration.restservice.AgentStatusWebService;
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
public class AgentRicaStatusService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AgentStatusWebService webService;

    @Value("${flash.rica.agent.status.production}")
    private String agentStatusProduction;

    @Value("${flash.rica.agent.status.sandbox}")
    private String agentStatusSandbox;

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();
    BuildHeader buildHeader = new BuildHeader();

    public String agentRicaStatus(Map<String, String> headers, AgentRicaStatusRequestDTO requestDTO){

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("Agent Rica Status");
        loggingPayloadMdl.setRequestInPayload(requestDTO);
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        JwtExtractor jwtExtractor = new JwtExtractor();

        String agentStatusURL = null;
        ResponseEntity<String> response;
        if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
            try {
                jwtExtractor.extract(headers);
                String environment = jwtExtractor.getKeyType();
                agentStatusURL = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(environment) ? agentStatusProduction : agentStatusSandbox;
                
            } catch (JSONException | JWTException e) {
                LOGGER.error("Agent Rica Status jwtExtractor: " + e.getLocalizedMessage());
                e.printStackTrace();
            }

            if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                response = new ResponseEntity<>("Can not access the required resource with the provided access token. Organization name missing.", HttpStatus.BAD_REQUEST);
                loggingPayloadMdl.setResponseOutPayload(response);
                loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                LOGGER.info("payload: "+JsonStringMapper.toJsonString(loggingPayloadMdl));
                return response.getBody();
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
        

        UriComponents urlTemplate = UriComponentsBuilder.fromHttpUrl(agentStatusURL)
                .path(requestDTO.getMsisdn())
                .build();

        HttpHeaders httpHeaders = buildHeader.build(headers);

        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        loggingPayloadMdl.setRequestOutPayload(urlTemplate);
        loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        response =  webService.agentRicaStatusBackendService(entity, urlTemplate.toString());

        loggingPayloadMdl.setApiResource(urlTemplate.toString());

        loggingPayloadMdl.setResponseInPayload(response);
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
