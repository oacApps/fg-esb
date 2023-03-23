package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

import java.util.Date;

@Data
public class VoucherMdl {
    private String pin;
    private String serialNumber;
    private Date expiryDate;
    private int amount;
}
