package co.za.flash.credential.management.helper.utils;

import co.za.flash.credential.management.config.WSO2Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenCredentialHelper {
    private String qaRestApiCredentialToken = "";
    private String liveRestApiCredentialToken = "";
    private String qaSoapApiCredentialToken = "";
    private String liveSoapApiCredentialToken = "";

    public TokenCredentialHelper() {
        try {
            Map<String, String> env = System.getenv();
            this.qaRestApiCredentialToken = env.get("CM_REST_CREDENTIAL_QA");
            this.liveRestApiCredentialToken = env.get("CM_REST_CREDENTIAL_Live");
            this.qaSoapApiCredentialToken = env.get("CM_SOAP_CREDENTIAL_QA");
            this.liveSoapApiCredentialToken = env.get("CM_SOAP_CREDENTIAL_Live");
        } catch (Exception e) {}
    }

    public String getCurrentCredential(WSO2Config config) {
        if (config == null || !config.isLoaded())
            return "";
        if (config.usingRestApi()) {
            // get rest api token
            if (config.usingSandbox()) {
                // we don't have any other env. return qa token here
                return qaRestApiCredentialToken;
            } else {
                // live token
                return liveRestApiCredentialToken;
            }
        } else {
            // get rest api token
            if (config.usingSandbox()) {
                // we don't have any other env. return qa token here
                return qaSoapApiCredentialToken;
            } else {
                // live token
                return liveSoapApiCredentialToken;
            }
        }
    }
}
