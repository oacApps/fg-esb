package co.za.flash.esb.flashvoucher.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private String serialNumber;
    private String pin;
    private AmountDTO amount;
    private String expiryDate;
}
