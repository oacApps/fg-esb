package co.za.flash.esb.oneforyou.trader.model.response;

import co.za.flash.esb.oneforyou.trader.model.shared.OneToken;
import lombok.Data;

@Data
public class PurchaseResponseMdl {
    private String actionCode;
    private String screenMessage;
    private Long sequenceNumber;
    private String receipt;
    private Long amountAuthorised;
    private Long balance;
    private Long availableBalance;
    private String currency;
    private String transactionReference;
    private String transactionDate;
    private OneToken oneToken;
    private String obResponseCode;
    private String obResponseMessage;
    private String obTransactionId;
}
