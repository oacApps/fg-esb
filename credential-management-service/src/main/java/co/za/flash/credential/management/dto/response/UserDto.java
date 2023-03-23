package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.utils.JsonParseUtil;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Data;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto implements IRestApiDto{
    // sample response:
    // {"emails":[{"type":"home","value":"kim.jackson@gmail.com","primary":true},
    // {"type":"work","value":"kim_j@wso2.com"}],"meta":{"created":"2018-08-15T14:55:23Z",
    // "location":"https://localhost:9443/scim2/Users/c8c821ba-1200-495e-a775-79b260e717bd",
    // "lastModified":"2018-08-15T14:55:23Z","resourceType":"User"},
    // "schemas":["urn:ietf:params:scim:schemas:core:2.0:User",
    // "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User"],
    // "name":{"familyName":"jackson","givenName":"kim"},"id":"c8c821ba-1200-495e-a775-79b260e717bd",
    // "userName":"kim"}
    private String id;
    private String userName;
    private List<String> roles;

    public UserDto(String id, String userName) {
        this.id = id;
        this.userName = userName;
        this.roles = List.of();
    }

    public UserDto(String id, String userName, List<String> roles) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
    }

    public static GeneralDtoResponse<UserDto> parseResponse(String rawResponse) {
        // manually convert the response
        try {
            // manually parse the response. In case the fields are optional
            JsonObject rootObj = JsonParser.parseString(rawResponse).getAsJsonObject();
            UserDto response = parseJson(rootObj);
            if (response != null)
                return new GeneralDtoResponse(response);
        } catch (JsonParseException je) {
            return new GeneralDtoResponse(je.getLocalizedMessage());
        }
        return new GeneralDtoResponse("Parsing error");
    }

    public static UserDto parseJson(JsonObject rootObj) throws JsonParseException{
        String id = JsonParseUtil.parseString(rootObj, "id", "");
        String userName = JsonParseUtil.parseString(rootObj, "userName", "");
        ArrayList<String> roleList = new ArrayList<>();
        JsonArray roles = JsonParseUtil.parseChildList(rootObj, "roles");
        if (roles != null && !roles.isJsonNull() && !roles.isEmpty()) {
            for (int i = 0; i < roles.size(); i++) {
                JsonObject item = roles.get(i).getAsJsonObject();
                String role = JsonParseUtil.parseString(item, "value", "");
                // "value": "Internal/everyone,Internal/scim-app-user"
                String[] roleArray = role.split(",");
                for (int j = 0; j < roleArray.length; j++) {
                    String itemRole = roleArray[j].replace(" ", "");

                    if (!StringUtil.isNullOrBlank(itemRole)) {
                        roleList.add(itemRole);
                    }
                }
            }
        }
        return new UserDto(id, userName, roleList);
    }

    public boolean isValid() {
        return !StringUtil.isNullOrBlank(userName) && roles != null;
    }
}
