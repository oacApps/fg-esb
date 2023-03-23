package co.za.flash.credential.management.model.request;

import co.za.flash.credential.management.helper.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordRequest {
    @JsonProperty("userName") // for soap api to identify the user
    private String userName;
    @JsonProperty("newPassword")
    private String newPassword;
    @JsonProperty("validatePassword")
    private boolean validatePassword = true; // optional. by default true
    @JsonIgnore
    private String userNameWithDomain;

    @JsonIgnore
    public boolean isValidInput() {
        return isValidUserName() && (!StringUtil.isNullOrBlank(newPassword));
    }

    @JsonIgnore
    private boolean isValidUserName() {
        return (!StringUtil.isNullOrBlank(userName)) &&
                !userName.contains("/");
    }

    @JsonIgnore
    public void setUserNameWithDomain(String userNameWithDomain) {
        this.userNameWithDomain = userNameWithDomain;
    }

    @JsonIgnore
    public String getUserNameWithDomain() {
        return userNameWithDomain;
    }

    @JsonIgnore
    public String getUserName() {
        return userName;
    }

    @JsonIgnore
    public String getNewPassword() {
        return newPassword;
    }

    @JsonIgnore
    public boolean isValidatePassword() {
        return validatePassword;
    }
}
