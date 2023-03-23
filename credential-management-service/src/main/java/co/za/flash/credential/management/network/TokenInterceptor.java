package co.za.flash.credential.management.network;

import co.za.flash.credential.management.config.WSO2Config;
import co.za.flash.credential.management.helper.utils.TokenCredentialHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TokenInterceptor implements ClientHttpRequestInterceptor {
    private String credentialToken;
    public TokenInterceptor(WSO2Config config) {
        credentialToken = new TokenCredentialHelper().getCurrentCredential(config);
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        // Todo 1. get valid token from the token store
        HttpHeaders headers = request.getHeaders();
        headers.setBasicAuth(credentialToken);
        ClientHttpResponse response = execution.execute(request, body);
        // Todo 2. when the response is back with specific error (unauth 401)
        // refresh the token and rerun the call.
        return response;
    }
}
