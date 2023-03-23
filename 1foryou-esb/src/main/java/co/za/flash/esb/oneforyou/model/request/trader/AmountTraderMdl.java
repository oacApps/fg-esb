package co.za.flash.esb.oneforyou.model.request.trader;

import lombok.Data;

@Data
public class AmountTraderMdl {
    private String currency;
    private Long value;
}
