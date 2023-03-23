package co.za.flash.credential.management.helper.enums;

import co.za.flash.credential.management.helper.utils.StringUtil;

public enum ErrorCodes {
    Default(999, "Default Error"),
    NotFound(1, "Not found record"),
    // section for rest related
    DefaultErrorFromRestClient(1000, "Invalid response from rest client"),
    EmptyResponseFromRestClient(1001, "Empty response from rest client"),
    InvalidResponseFromRestClient(1002, "Invalid response from rest client"),
    // section for soap related
    DefaultErrorFromSoapClient(1100, "Invalid response from soap client"),
    EmptyResponseFromSoapClient(1101, "Empty response from soap client"),
    InvalidResponseFromSoapClient(1102, "Invalid response from soap client"),
    // local issues
    WrongInput(2000, "Invalid input body"),
    WrongWso2Configuration(2001, "WSO2 config invalid for this action"),
    UserExists(2002, "User exists"),
    UserCannotChangePassword(2003, "User is not allowed to change password"),
    DomainNameMissing(2004, "Domain name is required"),
    // JSON parser
    JsonParseDefaultException(3000, "Could not parse the json object"),
    // Password validation
    InvalidPassword(4000, "Invalid password"),
    InvalidPasswordRule(4001, "Could not find the password validation rule"),
    // domain related
    InvalidDomain(5000, "Invalid domain access right"),
    MissingDomain(5001, "Domain must be set in the claim"),
    InvalidDomainAccessRight(5002, "Invalid domain access right");

    public final String description;
    public final int code;

    private ErrorCodes(int code, String description) {
        this.code = code;
        if (!StringUtil.isNullOrBlank(description)) {
            this.description = description;
        } else {
            this.description = "";
        }
    }
}
