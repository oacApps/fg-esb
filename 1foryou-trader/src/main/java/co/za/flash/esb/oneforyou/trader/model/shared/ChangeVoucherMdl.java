package co.za.flash.esb.oneforyou.trader.model.shared;

import lombok.Data;

@Data
public class ChangeVoucherMdl {
    private String voucherPin;
    private String voucherSerial;
    private String expiryDate; // "2023-04-28 04:10:14"
    private Long amount;
}
