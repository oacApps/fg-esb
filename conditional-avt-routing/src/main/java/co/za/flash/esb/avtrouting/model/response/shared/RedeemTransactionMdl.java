package co.za.flash.esb.avtrouting.model.response.shared;

import lombok.Data;

@Data
public class RedeemTransactionMdl {
    private String id;
    private String transactionGuid;
    private double amount;
    private String type;
    private String walletGuid;
    private int accountId;
    private String productCode;
    private int quantity;
    private String reference;
    private double fee;
    private Object commission;
    private int faceValue;
    private Object billingType;
    private String created;
}
