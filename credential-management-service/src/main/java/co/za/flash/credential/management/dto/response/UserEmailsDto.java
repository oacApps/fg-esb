package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.utils.JsonParseUtil;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sun.istack.NotNull;
import lombok.Data;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserEmailsDto {
    private List<String> emails;

    public UserEmailsDto() {
        this.emails = List.of();
    }

    public UserEmailsDto(@NotNull List<String> emails) {
        this.emails = emails;
    }

    public static GeneralDtoResponse<UserEmailsDto> parseResponse(String rawResponse) {
        try {
            // manually parse the response. In case the fields are optional
            JsonObject rootObj = JsonParser.parseString(rawResponse).getAsJsonObject();
            UserEmailsDto response = parse(rootObj);
            if (response != null && response.isValid())
                return new GeneralDtoResponse(response);
        } catch (JsonParseException je) {
            return new GeneralDtoResponse(je.getLocalizedMessage());
        }
        return new GeneralDtoResponse("Parsing error");
    }

    public static UserEmailsDto parse(JsonObject rootObj) throws JsonParseException{
        ArrayList<String> emailList = new ArrayList<>();
        JsonArray resources = JsonParseUtil.parseChildList(rootObj, "Resources");
        if (resources != null && !resources.isJsonNull() && !resources.isEmpty()) {
            JsonObject resource = resources.get(0).getAsJsonObject();
            JsonArray emails = JsonParseUtil.parseChildList(resource, "emails");
            if (emails != null && !emails.isJsonNull()) {
                emails.forEach(item -> {
                    if (item.isJsonPrimitive()) {
                        String mail = item.getAsString();
                        if (!StringUtil.isNullOrBlank(mail))
                            emailList.add(mail);
                    }
                });
            }
        }

        if (emailList.isEmpty())
            return new UserEmailsDto();
        return new UserEmailsDto(emailList);
    }

    public boolean isValid() {
        return emails.isEmpty() || !emails.stream().anyMatch(item -> StringUtil.isNullOrBlank(item));
    }

    public boolean hasData() {
        return !emails.isEmpty();
    }

    public static GeneralDtoResponse<UserEmailsDto> parseXml(String rawResponse, SoapEnvFieldNames rootField) {
        Node parentNode = SoapEnvelopeParser.getNodeFromBody(rawResponse, rootField.fieldName);
        if (parentNode != null) {
            ArrayList<String> emailList = new ArrayList<>();
            List<Node> list = SoapEnvelopeParser.getChildNodes(parentNode, SoapEnvFieldNames.NsReturn.fieldName);
            if (!list.isEmpty()) {
                list.stream().forEach(item -> {
                    String email = item.getTextContent();
                    if (!StringUtil.isNullOrBlank(email)) {
                        // remove comma in the end!
                        if (email.endsWith(","))
                            emailList.add(email.substring(0, email.length()-1));
                        else
                            emailList.add(email);
                    }
                });
            }
            if (!emailList.isEmpty()){
                return new GeneralDtoResponse(new UserEmailsDto(emailList));
            }
            return new GeneralDtoResponse(new UserEmailsDto());
        }
        return new GeneralDtoResponse("Parsing error");
    }
}
