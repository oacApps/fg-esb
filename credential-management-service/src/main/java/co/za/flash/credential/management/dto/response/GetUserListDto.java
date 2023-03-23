package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.utils.JsonParseUtil;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import com.google.gson.*;
import com.sun.istack.NotNull;
import lombok.Data;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class GetUserListDto {
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private List<UserDto> users;

    private GetUserListDto() {
        this.totalResults = 0;
        this.startIndex = 1;
        this.itemsPerPage = 0;
        this.users = List.of();
    }

    public GetUserListDto(@NotNull List<UserDto> users) {
        this.totalResults = users.size();
        this.startIndex = 1;
        this.itemsPerPage = users.size();
        this.users = users;
    }

    private GetUserListDto(
            int totalResults,
            int startIndex,
            int itemsPerPage,
            List<UserDto> users
    ) {
        this.totalResults = totalResults;
        this.startIndex = startIndex;
        this.itemsPerPage = itemsPerPage;
        this.users = users;
    }

    public static GeneralDtoResponse<GetUserListDto> parseResponse(String rawResponse) {
        try {
            // manually parse the response. In case the fields are optional
            JsonObject rootObj = JsonParser.parseString(rawResponse).getAsJsonObject();
            GetUserListDto response = parse(rootObj);
            if (response != null && response.isValid())
                return new GeneralDtoResponse(response);
        } catch (JsonParseException je) {
            return new GeneralDtoResponse(je.getLocalizedMessage());
        }
        return new GeneralDtoResponse("Parsing error");
    }

    public static GetUserListDto parse(JsonObject rootObj) throws JsonParseException {
        List<UserDto> users = new ArrayList<UserDto>();
        int totalResults = JsonParseUtil.parseInt(rootObj, "totalResults", 0);
        int startIndex = JsonParseUtil.parseInt(rootObj, "startIndex", 1);
        int itemsPerPage = JsonParseUtil.parseInt(rootObj, "itemsPerPage", 0);
        JsonArray usersArray = JsonParseUtil.parseChildList(rootObj, "Resources");
        if (usersArray != null && !usersArray.isJsonNull()) {
            usersArray.forEach(item -> {
                JsonObject child = item.getAsJsonObject();
                UserDto it = UserDto.parseJson(child);
                if (it != null && it.isValid()) {
                    users.add(it);
                }
            });
        }
        return new GetUserListDto(totalResults, startIndex, itemsPerPage, Collections.unmodifiableList(users));
    }

    public boolean isValid() {
        return hasData() || // has data
                (totalResults == 0 && users.isEmpty()); // empty list
    }

    public boolean hasData() {
        return (totalResults > 0 && startIndex > 0 && itemsPerPage > 0 && !users.isEmpty());
    }

    public static GeneralDtoResponse<GetUserListDto> parseXml(String rawResponse, SoapEnvFieldNames rootField) {
        Node parentNode = SoapEnvelopeParser.getNodeFromBody(rawResponse, rootField.fieldName);
        if (parentNode != null) {
            ArrayList<UserDto> userList = new ArrayList<>();
            List<Node> list = SoapEnvelopeParser.getChildNodes(parentNode, SoapEnvFieldNames.NsReturn.fieldName);
            if (!list.isEmpty()) {
                list.stream().forEach(item -> {
                    String userName = item.getTextContent();
                    if (!StringUtil.isNullOrBlank(userName)) {
                        userList.add(new UserDto("", userName));
                    }
                });
            }
            if (userList.isEmpty()){
                return new GeneralDtoResponse(new GetUserListDto());
            }
            return new GeneralDtoResponse(new GetUserListDto(userList));
        }
        /*<?xml version='1.0' encoding='UTF-8'?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
    <soapenv:Body>
        <ns:listUsersResponse xmlns:ns="http://service.ws.um.carbon.wso2.org" xmlns:ax2730="http://dao.service.ws.um.carbon.wso2.org/xsd" xmlns:ax2728="http://common.mgt.user.carbon.wso2.org/xsd" xmlns:ax2732="http://tenant.core.user.carbon.wso2.org/xsd" xmlns:ax2724="http://core.user.carbon.wso2.org/xsd" xmlns:ax2725="http://api.user.carbon.wso2.org/xsd">
            <ns:return>GENERALIDENTITIES/soap-addusertest</ns:return>
        </ns:listUsersResponse>
    </soapenv:Body>
</soapenv:Envelope>*/
        return new GeneralDtoResponse("Parsing error");
    }
}
