package co.za.flash.esb.giftvoucher.model.shared;

import lombok.Data;

@Data
public class TokenMdl {
    public long obTransactionId;
    public String pin;
    public String serialNumber;
    public int amountCents;
    public String referenceId;
    public String expiryDate;
}
