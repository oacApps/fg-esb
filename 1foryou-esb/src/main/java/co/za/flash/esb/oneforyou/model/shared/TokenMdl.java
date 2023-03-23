package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class TokenMdl {
    private int obTransactionId;
    private String pin;
    private String serialNumber;
    private Long amountCents;
    private String referenceId;
    private String expiryDate;
}
