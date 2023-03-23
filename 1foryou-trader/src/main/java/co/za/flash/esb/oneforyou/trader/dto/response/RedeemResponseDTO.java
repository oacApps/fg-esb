package co.za.flash.esb.oneforyou.trader.dto.response;

import co.za.flash.esb.oneforyou.trader.dto.shared.*;
import lombok.Data;

import java.util.List;

@Data
public class RedeemResponseDTO {
    private String responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate; //"2020-04-24 13:53:23.023",
    private Long sequenceNumber;
    private AmountDTO amount;
    private BalanceDTO balance;
    private AvailableBalanceDTO availableBalance;
    private Object acquirerReference;
    private ChangeVoucherDTO changeVoucher;
}
