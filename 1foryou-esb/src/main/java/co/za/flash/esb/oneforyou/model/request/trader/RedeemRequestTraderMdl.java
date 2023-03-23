package co.za.flash.esb.oneforyou.model.request.trader;


import co.za.flash.esb.oneforyou.model.shared.CustomerContactMdl;
import lombok.Data;

import java.util.List;

@Data
public class RedeemRequestTraderMdl {
    private String accountNumber;
    private Long sequenceNumber;
    private String tokenNumber;
    private AmountTraderMdl amount;
    private List<AcquirerTraderMdl> acquirerReference;
    private CustomerContactMdl customerContact;
    private String referenceId;
}
