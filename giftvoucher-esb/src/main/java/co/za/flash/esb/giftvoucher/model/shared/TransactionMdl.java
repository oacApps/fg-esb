package co.za.flash.esb.giftvoucher.model.shared;

import lombok.Data;

@Data
public class TransactionMdl {
    private String id;
    private String walletGuid;
    private int accountId;
    private String type;
    private String reference;
    private String created;
}
