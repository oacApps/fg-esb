package co.za.flash.esb.cellular.exceptions;

import co.za.flash.esb.library.errorhandler.ApiError;
import co.za.flash.esb.library.errorhandler.ApiSubError;
import co.za.flash.esb.library.errorhandler.ApiValidationError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Required field missing";

        ApiSubError apiSubError = new ApiSubError();
        List<ApiValidationError> apiValidationErrors = ex.getBindingResult().getFieldErrors().stream().map(tmp ->{
            ApiValidationError apiValidationError = new ApiValidationError(tmp.getObjectName(), tmp.getField(), tmp.getRejectedValue(), tmp.getDefaultMessage());
            return apiValidationError;
        }).collect(Collectors.toList());
        apiSubError.setValidationError(apiValidationErrors);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, apiSubError));
    }
    private ResponseEntity<Object> buildResponseEntity(List<ApiError> apiErrors, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiErrors, httpStatus);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
