package co.za.flash.esb.avtrouting.model.response.shared;

import lombok.Data;

@Data
public class WalletMdl {
    private int id;
    private String walletGuid;
    private int creditLimit;
    private double balance;
    private String name;
    private String reference;
    private int accountId;
    private double availableBalance;
}
