package co.za.flash.credential.management.model.response;

import co.za.flash.credential.management.dto.response.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Locale;

@Data
public class UserResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("userNameWithDomain")
    private String userNameWithDomain;
    @JsonProperty("isSelfManageable")
    private boolean isSelfManageable;

    public UserResponse(UserDto dtoResponse, String roleName) {
        this.id = dtoResponse.getId();
        this.userNameWithDomain = dtoResponse.getUserName();
        if (dtoResponse.getRoles().isEmpty()) {
            this.isSelfManageable = false;
        } else {
            this.isSelfManageable = dtoResponse.getRoles().stream().anyMatch(item ->
                    item.equals(roleName));
        }
    }
}
