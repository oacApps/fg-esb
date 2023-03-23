package co.za.flash.esb.oneforyou.trader.dto.response;

import co.za.flash.esb.oneforyou.trader.dto.shared.*;
import lombok.Data;

import java.util.List;

@Data
public class RefundRedemptionResponseMatchWsoDTO {
    private String responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate; //"2020-04-24 13:53:23.023",
    private Long sequenceNumber;
    private BalanceDTO balance;
    private AmountDTO amount;
    private AvailableBalanceDTO availableBalance;
    private Object acquirerReference;
    private ChangeVoucherWsoDTO changeVoucher;
}

