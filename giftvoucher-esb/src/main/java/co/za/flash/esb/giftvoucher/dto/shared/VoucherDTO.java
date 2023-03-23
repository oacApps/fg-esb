package co.za.flash.esb.giftvoucher.dto.shared;

import lombok.Data;

import java.math.BigInteger;

@Data
public class VoucherDTO {
    private String pin;
    private BigInteger serialNumber;
    private String expiryDate;
    private int amount;
}
