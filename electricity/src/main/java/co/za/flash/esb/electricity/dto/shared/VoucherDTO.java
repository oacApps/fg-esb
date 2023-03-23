package co.za.flash.esb.electricity.dto.shared;

import lombok.Data;

import java.util.Date;

@Data
public class VoucherDTO {
    private String pin;
    private String serialNumber;
    private Date expiryDate;
    private int amount;
}
