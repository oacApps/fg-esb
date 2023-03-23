package co.za.flash.esb.flashvoucher.model;

import lombok.Data;

@Data
public class RequestMDL {
    private String sequenceNumber;
    private String currency;
    private String amountRequested;
    private UserMDL user;
}
