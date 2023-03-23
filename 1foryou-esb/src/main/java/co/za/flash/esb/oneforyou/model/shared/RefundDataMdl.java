package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class RefundDataMdl {
    private String requestId;
    private String redemptionRequestId;
    private Long amount;
    private String voucherPin;
    private String voucherSerial;
    private String expiryDate;
    private String transactionDate;
    private WalletMdl wallet;
}
