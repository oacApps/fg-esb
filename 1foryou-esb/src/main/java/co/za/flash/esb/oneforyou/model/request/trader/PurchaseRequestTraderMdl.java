package co.za.flash.esb.oneforyou.model.request.trader;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestTraderMdl {
    private String accountNumber;
    private Long sequenceNumber;
    private AmountTraderMdl amount;
    private List<AcquirerTraderMdl> acquirerReference;
}

