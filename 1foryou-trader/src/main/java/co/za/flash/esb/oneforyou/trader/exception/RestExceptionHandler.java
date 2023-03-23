package co.za.flash.esb.oneforyou.trader.exception;

import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.errorhandler.ApiError;
import co.za.flash.esb.library.errorhandler.ApiSubError;
import co.za.flash.esb.library.errorhandler.ApiValidationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.ObjectToStringHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        LOGGER.info("Error : " + ex.getLocalizedMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ""));
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

    @ExceptionHandler(value = Exception.class)
    public Map<String,String> handleException(Exception e, HttpServletRequest request){


        Map<String, Object> requestMap = null;
        try {
            requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        LOGGER.error("Exception occured for the request:"+requestMap);
        LOGGER.error("Exception is "+e.getMessage());

        Map<String,String> response = new HashMap();

        response.put("status", "failure");

        return response;

    }
}
