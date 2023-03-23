package co.za.flash.esb.avtrouting.model.request;

import lombok.Data;

@Data
public class RedeemRequestMdl {
    private String accountNumber;
    private String referenceId;
    private int amountCents;
    private String pin;
    private String userId;
}
