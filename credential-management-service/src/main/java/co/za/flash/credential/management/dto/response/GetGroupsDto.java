package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.utils.JsonParseUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class GetGroupsDto {
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private List<GroupDto> groups;

    private GetGroupsDto(int totalResults, int startIndex, int itemsPerPage, List<GroupDto> groups) {
        this.totalResults = totalResults;
        this.startIndex = startIndex;
        this.itemsPerPage = itemsPerPage;
        this.groups = groups;
    }

    public static GeneralDtoResponse<GetGroupsDto> parseResponse(String rawResponse) {
        try {
            // manually parse the response. In case the fields are optional
            JsonObject rootObj = JsonParser.parseString(rawResponse).getAsJsonObject();
            GetGroupsDto response = parse(rootObj);
            if (response != null && response.isValid())
                return new GeneralDtoResponse(response);
        } catch (JsonParseException je) {
            return new GeneralDtoResponse(je.getLocalizedMessage());
        }
        return new GeneralDtoResponse("Parsing error");
    }

    public static GetGroupsDto parse(JsonObject rootObj) {
        List<GroupDto> groups = new ArrayList<GroupDto>();
        int totalResults = JsonParseUtil.parseInt(rootObj, "totalResults", 0);
        int startIndex = JsonParseUtil.parseInt(rootObj, "startIndex", 1);
        int itemsPerPage = JsonParseUtil.parseInt(rootObj, "itemsPerPage", 0);
        JsonArray usersArray = JsonParseUtil.parseChildList(rootObj, "Resources");
        if (usersArray != null && !usersArray.isJsonNull()) {
            usersArray.forEach(item -> {
                JsonObject child = item.getAsJsonObject();
                GroupDto it = GroupDto.parse(child);
                if (it != null && it.isValid()) {
                    groups.add(it);
                }
            });
        }
        return new GetGroupsDto(totalResults, startIndex, itemsPerPage, Collections.unmodifiableList(groups));
    }

    public boolean isValid() {
        return hasData() || // has data
                (totalResults == 0 && groups.isEmpty()); // empty list
    }

    public boolean hasData() {
        return (totalResults > 0 && startIndex > 0 && itemsPerPage > 0 && !groups.isEmpty());
    }
}
