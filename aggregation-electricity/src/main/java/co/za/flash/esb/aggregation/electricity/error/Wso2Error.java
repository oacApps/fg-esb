package co.za.flash.esb.aggregation.electricity.error;

import org.springframework.stereotype.Component;

@Component
public class Wso2Error {

    public Wso2ErrorMatchDTO invalidCredentials(){
        Wso2ErrorMatchDTO wso2ErrorMatchDTO = new Wso2ErrorMatchDTO();
        Wso2FaultDTO wso2FaultDTO = new Wso2FaultDTO();
        wso2FaultDTO.setCode(900901);
        wso2FaultDTO.setType("Status report");
        wso2FaultDTO.setMessage("Invalid Credentials");
        wso2FaultDTO.setDescription("Access failure for API: /ssc/1.0.1, version: 1.0.1 status: (900901) - Invalid Credentials. Make sure you have given the correct access token");
        wso2ErrorMatchDTO.setFault(wso2FaultDTO);
        return wso2ErrorMatchDTO;
    }

    public Wso2ErrorMatchDTO jwtMissing(){
        Wso2ErrorMatchDTO wso2ErrorMatchDTO = new Wso2ErrorMatchDTO();
        Wso2FaultDTO wso2FaultDTO = new Wso2FaultDTO();
        wso2FaultDTO.setCode(900401);
        wso2FaultDTO.setType("Status report");
        wso2FaultDTO.setMessage("Invalid Credentials");
        wso2FaultDTO.setDescription("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
        wso2ErrorMatchDTO.setFault(wso2FaultDTO);
        return wso2ErrorMatchDTO;
    }
}
