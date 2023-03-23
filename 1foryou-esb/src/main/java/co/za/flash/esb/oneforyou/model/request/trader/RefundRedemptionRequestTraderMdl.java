package co.za.flash.esb.oneforyou.model.request.trader;


import lombok.Data;

import java.util.List;

@Data
public class RefundRedemptionRequestTraderMdl {
    private String accountNumber;
    private Long sequenceNumber;
    private String redemptionTransactionId;
    private AmountTraderMdl amount;
    private List<AcquirerTraderMdl> acquirerReference;
}
