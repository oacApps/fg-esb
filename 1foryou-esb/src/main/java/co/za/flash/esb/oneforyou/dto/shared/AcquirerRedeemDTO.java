package co.za.flash.esb.oneforyou.dto.shared;

import lombok.Data;

@Data
public class AcquirerRedeemDTO {
    private AccountTraderDTO account;
    private String entityTag;
}
