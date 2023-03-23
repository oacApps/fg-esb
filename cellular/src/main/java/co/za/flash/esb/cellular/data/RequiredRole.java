package co.za.flash.esb.cellular.data;

import co.za.flash.esb.cellular.dto.Wso2ErrorMatchDTO;
import co.za.flash.esb.cellular.dto.Wso2FaultDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class RequiredRole {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    public boolean validateAccess(String requestedRole, JSONArray jwtRoles){
        if(allRoles().contains(requestedRole)){
            return searchInJsonArray(jwtRoles, requestedRole);
        }
        return false;
    }

    private boolean searchInJsonArray(JSONArray jwtRoles, String requestedRole){
        for (int i =0; i < jwtRoles.length(); i++){
            try {
                if(requestedRole.equals((String.valueOf(jwtRoles.get(i)).toLowerCase()))) return true;
            } catch (JSONException e) {
                LOGGER.error("Role based Access: Customer doesn't have enough access; trying to access : " + requestedRole + " is not found in JWT");
                LOGGER.error(e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    public Wso2ErrorMatchDTO wso2MatchError(){
        Wso2ErrorMatchDTO wso2ErrorMatchDTO = new Wso2ErrorMatchDTO();
        Wso2FaultDTO wso2FaultDTO = new Wso2FaultDTO();
        wso2FaultDTO.setType("Status report");
        wso2FaultDTO.setMessage("Invalid Credentials");
        wso2FaultDTO.setDescription("You do not have access for this selection. Please contact your Integration Specialist for more information.");
        wso2ErrorMatchDTO.setFault(wso2FaultDTO);
        return wso2ErrorMatchDTO;
    }

    private List allRoles(){
        List roles = new ArrayList();
        roles.add("cellular-mtn-pinless-airtime");
        roles.add("cellular-mtn-pin-airtime");
        roles.add("cellular-mtn-pinless-data");
        roles.add("cellular-mtn-pin-data");

        roles.add("cellular-vcom-pinless-airtime");
        roles.add("cellular-vcom-pin-airtime");
        roles.add("cellular-vcom-pinless-data");
        roles.add("cellular-vcom-pin-data");

        roles.add("cellular-cellc-pinless-airtime");
        roles.add("cellular-cellc-pin-airtime");
        roles.add("cellular-cellc-pinless-data");
        roles.add("cellular-cellc-pin-data");

        roles.add("cellular-vmob-pinless-airtime");
        roles.add("cellular-vmob-pin-airtime");
        roles.add("cellular-vmob-pinless-data");
        roles.add("cellular-vmob-pin-data");

        roles.add("cellular-tkom-pinless-airtime");
        roles.add("cellular-tkom-pin-airtime");
        roles.add("cellular-tkom-pinless-data");
        roles.add("cellular-tkom-pin-data");

        return roles;
    }
}
