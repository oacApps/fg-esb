package co.za.flash.esb.oneforyou.trader.dto.shared;

import lombok.Data;

@Data
public class BalanceDTO {
    private String currency;
    private Long value;
}
