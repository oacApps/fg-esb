package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class DataMdl {
    private TokenMdl token;
    private TransactionMdl transaction;
    private PurchaseMdl purchase;
}
