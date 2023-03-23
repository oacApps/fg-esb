package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class ChangeVoucherMdl {
    private Long amountCents;
    private String pin;
    private String serialNumber;
    private String expiryDate;
}

