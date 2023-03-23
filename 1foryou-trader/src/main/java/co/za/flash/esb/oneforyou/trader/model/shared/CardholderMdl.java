package co.za.flash.esb.oneforyou.trader.model.shared;

import lombok.Data;

@Data
public class CardholderMdl {
    private String msisdn;
    private String primaryAccountNumber;
    private CardMdl card;
}
