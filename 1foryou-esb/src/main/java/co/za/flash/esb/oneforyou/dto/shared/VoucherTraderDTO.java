package co.za.flash.esb.oneforyou.dto.shared;

import lombok.Data;

@Data
public class VoucherTraderDTO {
    private String pin;
    private String serialNumber;
    private String expiryDate;
    private String amount;
}
