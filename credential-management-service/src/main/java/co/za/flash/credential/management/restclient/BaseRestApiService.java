package co.za.flash.credential.management.restclient;

import co.za.flash.credential.management.dto.response.GeneralDtoResponse;
import co.za.flash.credential.management.helper.enums.ErrorCodes;
import co.za.flash.credential.management.helper.interfaces.IApiResponseHandler;
import co.za.flash.credential.management.helper.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class BaseRestApiService {
    public boolean isEndpointValid(String fullEndpointUrl) {
        // this call can be override for a specific endpoint check
        if (StringUtil.isNullOrBlank(fullEndpointUrl))
            return false;
        try {
            URL u = new URL(fullEndpointUrl); // this would check for the protocol
            URI uri = u.toURI();
        } catch (URISyntaxException uriException) {
            logError(fullEndpointUrl + ": invalid url");
            // invalid uri
            return false;
        } catch (Exception e) {
            logError(fullEndpointUrl + ": Exception:" + e.getLocalizedMessage());
            // log the error
            return false;
        }
        return true;
    }
    public abstract void logError(String errorMessage);

    protected GeneralDtoResponse<String> handleResponse(IApiResponseHandler handler, ResponseEntity<String> rawResponse) {
        if (rawResponse == null) {
            return new GeneralDtoResponse(ErrorCodes.DefaultErrorFromRestClient.description);
        } else if (handler.isDtoResponseSucceeded(rawResponse)) {
            return new GeneralDtoResponse(null, rawResponse.getBody());
        } else {
            String errorMessage = handler.getErrorMessage(rawResponse);
            return new GeneralDtoResponse(errorMessage);
        }
    }
}
