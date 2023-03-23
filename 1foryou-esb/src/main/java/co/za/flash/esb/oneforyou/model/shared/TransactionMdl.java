package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionMdl {
    private String id;
    private String walletGuid;
    private int accountId;
    private String transactionTypeId;
    private String reference;
    private String created;
}
