package co.za.flash.esb.oneforyou.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class OrganizationNotFound extends Exception{
    public OrganizationNotFound() {
        super("Organization Name Not found in your JWT token");
    }
}
