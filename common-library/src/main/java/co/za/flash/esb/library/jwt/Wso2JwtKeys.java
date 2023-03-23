package co.za.flash.esb.library.jwt;

import org.springframework.stereotype.Component;

@Component
public class Wso2JwtKeys {

    // Header
    public String xJwtAssertion      = "x-jwt-assertion";

    // payload
    public String organization       = "http://wso2.org/claims/organization";
    public String role               = "http://wso2.org/claims/role";
    public String applicationTier    = "http://wso2.org/claims/applicationtier";
    public String keyType            = "http://wso2.org/claims/keytype";
    public String version            = "http://wso2.org/claims/version";
    public String iss                = "iss";
    public String applicationName    = "http://wso2.org/claims/applicationname";
    public String endUser            = "http://wso2.org/claims/enduser";
    public String endUserTenantID    = "http://wso2.org/claims/enduserTenantId";
    public String givenName          = "http://wso2.org/claims/givenname";
    public String subscriber         = "http://wso2.org/claims/subscriber";
    public String tier               = "http://wso2.org/claims/tier";
    public String emailAddress       = "http://wso2.org/claims/emailaddress";
    public String lastName           = "http://wso2.org/claims/lastname";
    public String exp                = "exp";
    public String applicationID      = "http://wso2.org/claims/applicationid";
    public String userType           = "http://wso2.org/claims/usertype";
    public String apiContext         = "http://wso2.org/claims/apicontext";
}
