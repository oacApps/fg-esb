package co.za.flash.esb.electricity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends RestClientException {
    public ServiceUnavailableException() {
        super(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
    }
}
