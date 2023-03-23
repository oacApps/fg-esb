package co.za.flash.esb.oneforyou.trader.dto.response;

import co.za.flash.esb.oneforyou.trader.dto.shared.AvailableBalanceDTO;
import co.za.flash.esb.oneforyou.trader.dto.shared.BalanceDTO;
import lombok.Data;

@Data
public class ReversalsRedemptionResponseDTO {
    private String responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate; //"2020-04-24 13:53:23.023",
    private Long sequenceNumber;
    private BalanceDTO balance;
    private AvailableBalanceDTO availableBalance;
    private Object acquirerReference;
}

