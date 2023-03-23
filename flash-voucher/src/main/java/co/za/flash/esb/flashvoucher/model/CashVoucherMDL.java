package co.za.flash.esb.flashvoucher.model;

import lombok.Data;

@Data
public class CashVoucherMDL {
    private long id;
    private String voucherType;
    private String serialNumber;
    private String pin;
    private String currencyCode;
    private long amount;
    private String expiryDate;
    private String accountDispensed;
    private String accountRedeemed;
    private Long cancelledReason;
    private String transactionReferenceDispensed;
    private String transactionReferenceRedeemed;
    private String transactionReferenceRefunded;
}
