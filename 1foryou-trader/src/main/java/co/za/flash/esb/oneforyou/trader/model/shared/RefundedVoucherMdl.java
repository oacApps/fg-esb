package co.za.flash.esb.oneforyou.trader.model.shared;

import lombok.Data;

@Data
public class RefundedVoucherMdl {
    private String pin;
    private String serialNumber;
    private String expiryDate;
    private String expiryDateIso;
    private String extraInfo;
    private int status;
    private Long amount;
}
