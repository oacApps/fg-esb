package co.za.flash.credential.management.dto.request.rest;

import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.model.request.AddUserRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class UserDto {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("password")
    private String password;
    /*@JsonProperty("emails")
    private ArrayList<OrderMapDto> emails = null;*/

    public UserDto(AddUserRequest request, String defaultDomain) {
        String domain = defaultDomain;
        if (!StringUtil.isNullOrBlank(request.getDomain())) {
            domain = request.getDomain();
        }
        this.userName = domain + "/" + request.getUsername();
        this.password = request.getPassword();
        //setEmail(request.getEmail());
    }

    /*private void setEmail(String email) {
        if (!StringUtil.isNullOrBlank(email)) {
            emails = new ArrayList<>();
            if (!StringUtil.isNullOrBlank(email)) {
                OrderMapDto dto = new OrderMapDto("default", email, true);
                emails.add(dto);
            }
        }
    }*/
}
