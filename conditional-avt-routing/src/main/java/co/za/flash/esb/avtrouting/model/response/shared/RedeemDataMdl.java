package co.za.flash.esb.avtrouting.model.response.shared;

import lombok.Data;

@Data
public class RedeemDataMdl {
    private String referenceId;
    private String redemptionRequestId;
    private long switchId;
    private int amountCents;
    private ChangeVoucherMdl changeVoucher;
    private RedeemTransactionMdl transaction;
    private WalletMdl wallet;
}
