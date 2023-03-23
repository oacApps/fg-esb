package co.za.flash.credential.management.model.request;

import co.za.flash.credential.management.helper.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateEmailRequest {
    @JsonProperty("userNameWithDomain")
    private String userNameWithDomain;
    @JsonProperty("email")
    private String email; // only one email can be set for an account
    @JsonProperty("isUpdate")
    private boolean isUpdate; // true: add email; false: update email

    @JsonIgnore
    public boolean isValidInput() {
        return (!StringUtil.isNullOrBlank(userNameWithDomain)) && (!StringUtil.isNullOrBlank(email)
        && StringUtil.isValidEmail(email));
    }

    @JsonIgnore
    public String getUserNameWithDomain() {
        return userNameWithDomain;
    }

    @JsonIgnore
    public void setUserNameWithDomain(String userNameWithDomain) {
        this.userNameWithDomain = userNameWithDomain;
    }

    @JsonIgnore
    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public boolean isUpdate() {
        return isUpdate;
    }

    @JsonIgnore
    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
