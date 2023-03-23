package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class RedeemDataMdl {
    private String referenceId;
    private String redemptionRequestId;
    private int switchId;
    private Long amountCents;
    private ChangeVoucherMdl changeVoucher;
    private RedeemTransactionMdl transaction;
    private WalletMdl wallet;
}
