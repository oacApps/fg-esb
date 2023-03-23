package co.za.flash.esb.library.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JWTException extends Exception{
    public JWTException(){
        super("Invalid JWT Token");
    }
}
