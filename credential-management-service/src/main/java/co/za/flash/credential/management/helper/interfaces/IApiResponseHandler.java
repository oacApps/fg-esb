package co.za.flash.credential.management.helper.interfaces;

import co.za.flash.credential.management.dto.response.GeneralDtoResponse;
import co.za.flash.credential.management.helper.enums.ErrorCodes;
import org.springframework.http.ResponseEntity;

public interface IApiResponseHandler {

    public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse);

    public String getErrorMessage(ResponseEntity<String> rawResponse);
}
