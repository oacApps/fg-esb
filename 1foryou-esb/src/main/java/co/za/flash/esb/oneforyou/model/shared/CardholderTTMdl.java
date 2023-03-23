package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class CardholderTTMdl {
    private String msisdn;
    private String primaryAccountNumber;
    private CardTTMdl card;
}
