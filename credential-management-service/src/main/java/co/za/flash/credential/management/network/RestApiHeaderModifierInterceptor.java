package co.za.flash.credential.management.network;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class RestApiHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        if (request.getMethod() != HttpMethod.GET) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }
}
