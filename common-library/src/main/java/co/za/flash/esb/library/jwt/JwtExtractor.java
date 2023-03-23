package co.za.flash.esb.library.jwt;


import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@Component
@Getter
public class JwtExtractor {

    private String organization;
    private JSONArray role;
    private String applicationTier;
    private String keyType;
    private String version;

    private String iss;
    private String applicationName;
    private String endUser;
    private String endUserTenantID;
    private String givenName;
    private String subscriber;
    private String tier;
    private String emailAddress;
    private String lastName;
    private String exp;
    private String applicationID;
    private String userType;
    private String apiContext;

    public void extract(Map<String, String> headers) throws JSONException, JWTException {

        Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

        if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {

            String jwtToken = headers.get(wso2JwtKeys.xJwtAssertion);
            String[] chunks = jwtToken.split("\\.");

            if (chunks.length > 1) {
                byte[] decodedBytes = Base64.getMimeDecoder().decode(chunks[1]);
                String payload = new String(decodedBytes);
                JSONObject json = new JSONObject(payload);

                if (json.has(wso2JwtKeys.organization)) {
                    this.organization = String.valueOf(json.get(wso2JwtKeys.organization));
                }

                if (json.has(wso2JwtKeys.role)) {
                    this.role = json.getJSONArray(wso2JwtKeys.role);
                }

                if (json.has(wso2JwtKeys.keyType)) {
                    this.keyType = String.valueOf(json.get(wso2JwtKeys.keyType));
                }

                if (json.has(wso2JwtKeys.applicationTier)) {
                    this.applicationTier = String.valueOf(json.get(wso2JwtKeys.applicationTier));
                }

                if (json.has(wso2JwtKeys.version)) {
                    this.version = String.valueOf(json.get(wso2JwtKeys.version));
                }

                if (json.has(wso2JwtKeys.iss)) {
                    this.iss = String.valueOf(json.get(wso2JwtKeys.iss));
                }

                if (json.has(wso2JwtKeys.applicationName)) {
                    this.applicationName = String.valueOf(json.get(wso2JwtKeys.applicationName));
                }

                if (json.has(wso2JwtKeys.endUser)) {
                    this.endUser = String.valueOf(json.get(wso2JwtKeys.endUser));
                }

                if (json.has(wso2JwtKeys.endUserTenantID)) {
                    this.endUserTenantID = String.valueOf(json.get(wso2JwtKeys.endUser));
                }

                if (json.has(wso2JwtKeys.givenName)) {
                    this.givenName = String.valueOf(json.get(wso2JwtKeys.givenName));
                }

                if (json.has(wso2JwtKeys.subscriber)) {
                    this.subscriber = String.valueOf(json.get(wso2JwtKeys.subscriber));
                }

                if (json.has(wso2JwtKeys.tier)) {
                    this.tier = String.valueOf(json.get(wso2JwtKeys.tier));
                }

                if (json.has(wso2JwtKeys.emailAddress)) {
                    this.emailAddress = String.valueOf(json.get(wso2JwtKeys.emailAddress));
                }

                if (json.has(wso2JwtKeys.lastName)) {
                    this.lastName = String.valueOf(json.get(wso2JwtKeys.lastName));
                }

                if (json.has(wso2JwtKeys.exp)) {
                    this.exp = String.valueOf(json.get(wso2JwtKeys.exp));
                }

                if (json.has(wso2JwtKeys.applicationID)) {
                    this.applicationID = String.valueOf(json.get(wso2JwtKeys.applicationID));
                }

                if (json.has(wso2JwtKeys.userType)) {
                    this.userType = String.valueOf(json.get(wso2JwtKeys.userType));
                }

                if (json.has(wso2JwtKeys.apiContext)) {
                    this.apiContext = String.valueOf(json.get(wso2JwtKeys.apiContext));
                }


            } else {
                throw new JWTException();
            }
        }
    }
}
