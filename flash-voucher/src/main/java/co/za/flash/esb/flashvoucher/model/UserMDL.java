package co.za.flash.esb.flashvoucher.model;

import lombok.Data;

@Data
public class UserMDL {
    private AuthenticationMDL authentication;
    private String purseAccountNumber;
}
