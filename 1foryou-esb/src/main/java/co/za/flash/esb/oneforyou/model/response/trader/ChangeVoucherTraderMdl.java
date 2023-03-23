package co.za.flash.esb.oneforyou.model.response.trader;

import lombok.Data;

@Data
public class ChangeVoucherTraderMdl {
    private String pin;
    private String serialNumber;
    private int status;
    private String expiryDate; // "2023-04-28 04:10:14"
    private AmountTraderMdl amount;
}
