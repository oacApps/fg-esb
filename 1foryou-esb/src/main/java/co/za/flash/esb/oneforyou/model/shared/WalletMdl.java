package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class WalletMdl {
    private int id;
    private String walletGuid;
    private Long creditLimit;
    private Long balance;
    private String name;
    private String reference;
    private int accountId;
    private Long availableBalance;
}
