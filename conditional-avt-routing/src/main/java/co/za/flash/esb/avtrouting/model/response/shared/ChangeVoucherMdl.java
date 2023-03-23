package co.za.flash.esb.avtrouting.model.response.shared;

import lombok.Data;

@Data
public class ChangeVoucherMdl {
    private int amountCents;
    private String pin;
    private String serialNumber;
    private String expiryDate;
}

