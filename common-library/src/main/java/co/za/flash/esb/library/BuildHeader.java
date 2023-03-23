package co.za.flash.esb.library;

import co.za.flash.esb.library.enums.HeadersKey;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public class BuildHeader {

    public HttpHeaders build(Map<String, String> headers){
        HttpHeaders httpHeaders = new HttpHeaders();
        if(headers.containsKey(HeadersKey.X_JWT_ASSERTION.getValue())){
            httpHeaders.set(HeadersKey.X_JWT_ASSERTION.getValue(), headers.get(HeadersKey.X_JWT_ASSERTION.getValue()));
        }
        if(headers.containsKey(HeadersKey.AUTHORIZATION.getValue())){
            httpHeaders.set(HeadersKey.AUTHORIZATION.getValue(), headers.get(HeadersKey.AUTHORIZATION.getValue()));
        }

        httpHeaders.set(HeadersKey.CONTENT_TYPE.getValue(), HeadersKey.MEDIA_TYPE_APPLICATION_JSON.getValue());
        httpHeaders.set(HeadersKey.ACCEPT.getValue(), HeadersKey.MEDIA_TYPE_APPLICATION_JSON.getValue());

        return httpHeaders;

    }

}
