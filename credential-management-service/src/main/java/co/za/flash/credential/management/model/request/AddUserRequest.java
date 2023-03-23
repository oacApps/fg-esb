package co.za.flash.credential.management.model.request;

import co.za.flash.credential.management.helper.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddUserRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonIgnore
    private String domain = ""; // optional. by default will be empty and use the configed domain
    @JsonProperty("selfManageable")
    private boolean selfManageable = true; // optional. by default true
    @JsonProperty("validatePassword")
    private boolean validatePassword = true; // optional. by default true

    @JsonIgnore
    public boolean isValidInput() {
        return (!StringUtil.isNullOrBlank(username)) && (!StringUtil.isNullOrBlank(password));
    }

    @JsonIgnore
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public String getDomain() {
        return domain;
    }

    @JsonIgnore
    public boolean isSelfManageable() {
        return selfManageable;
    }

    @JsonIgnore
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @JsonIgnore
    public boolean isValidatePassword() {
        return validatePassword;
    }
}
