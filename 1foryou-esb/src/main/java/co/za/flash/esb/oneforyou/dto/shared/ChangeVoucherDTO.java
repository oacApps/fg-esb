package co.za.flash.esb.oneforyou.dto.shared;

import lombok.Data;

@Data
public class ChangeVoucherDTO {
    private String pin;
    private String serialNumber;
    private String expiryDate;
    private Long amount;
}

