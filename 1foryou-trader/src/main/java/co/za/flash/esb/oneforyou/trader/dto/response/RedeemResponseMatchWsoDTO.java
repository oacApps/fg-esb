package co.za.flash.esb.oneforyou.trader.dto.response;

import co.za.flash.esb.oneforyou.trader.dto.shared.AmountWsoDTO;
import co.za.flash.esb.oneforyou.trader.dto.shared.ChangeVoucherDTO;
import lombok.Data;

@Data
public class RedeemResponseMatchWsoDTO {
    private String responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate; //"2020-04-24 13:53:23.023",
    private String sequenceNumber;
    private AmountWsoDTO amount;
    private AmountWsoDTO balance;
    private AmountWsoDTO availableBalance;
    private Object acquirerReference;
    private ChangeVoucherDTO changeVoucher;
}

