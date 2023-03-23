package co.za.flash.credential.management.model.request;

import co.za.flash.credential.management.helper.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordRequest {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("currentPassword")
    private String currentPassword;
    @JsonProperty("newPassword")
    private String newPassword;
    @JsonProperty("validatePassword")
    private boolean validatePassword = true; // optional. by default true
    @JsonIgnore
    private String userNameWithDomain;

    @JsonIgnore
    public void setUserNameWithDomain(String userNameWithDomain) {
        this.userNameWithDomain = userNameWithDomain;
    }

    @JsonIgnore
    public String getUserNameWithDomain() {
        return userNameWithDomain;
    }

    @JsonIgnore
    public boolean isValidInput() {
        return isValidUserName() &&
                (!StringUtil.isNullOrBlank(currentPassword) &&
                (!StringUtil.isNullOrBlank(newPassword)));
    }

    @JsonIgnore
    private boolean isValidUserName() {
        return (!StringUtil.isNullOrBlank(userName)) &&
                !userName.contains("/");
    }

    @JsonIgnore
    public String getUserName() {
        return userName;
    }

    @JsonIgnore
    public String getCurrentPassword() {
        return currentPassword;
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
