package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.utils.JsonParseUtil;
import co.za.flash.credential.management.helper.utils.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class GroupDto {
    private String displayName;
    private String id;
    private List<MemberDto> members;

    private GroupDto(String displayName, String id, List<MemberDto> members) {
        this.displayName = displayName;
        this.id = id;
        this.members = members;
    }

    public static GeneralDtoResponse<GroupDto> parseResponse(String rawResponse) {
        try {
            // manually parse the response. In case the fields are optional
            JsonObject rootObj = JsonParser.parseString(rawResponse).getAsJsonObject();
            GroupDto response = parse(rootObj);
            if (response != null && response.isValid())
                return new GeneralDtoResponse(response);
        } catch (JsonParseException je) {
            return new GeneralDtoResponse(je.getLocalizedMessage());
        }
        return new GeneralDtoResponse("Parsing error");
    }

    public static GroupDto parse(JsonObject rootObj) {
        List<MemberDto> members = new ArrayList<MemberDto>();
        String displayName = JsonParseUtil.parseString(rootObj, "displayName", "");
        String id = JsonParseUtil.parseString(rootObj, "id", "");
        JsonArray usersArray = JsonParseUtil.parseChildList(rootObj, "members");
        if (usersArray != null && !usersArray.isJsonNull()) {
            usersArray.forEach(item -> {
                JsonObject child = item.getAsJsonObject();
                MemberDto it = MemberDto.parse(child);
                if (it != null && it.isValid()) {
                    members.add(it);
                }
            });
        }
        return new GroupDto(displayName, id, Collections.unmodifiableList(members));
    }
    public boolean isValid() {
        return (!StringUtil.isNullOrBlank(displayName) && !StringUtil.isNullOrBlank(id));
    }
}
