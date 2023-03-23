package co.za.flash.esb.oneforyou.model.response.trader;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseResponseTraderMdl {
    private String responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate; //"2020-04-24 13:53:23.023",
    private Long sequenceNumber;
    private AmountTraderMdl balance;
    private AmountTraderMdl availableBalance;
    private List<AcquirerTraderMdl> acquirerReference;
    private OneTokenTraderMdl token;

}

