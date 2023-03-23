package co.za.flash.esb.oneforyou.model.request;

import co.za.flash.esb.oneforyou.model.shared.AcquirerReferenceMdl;
import lombok.Data;

import java.util.List;

@Data
public class RedeemRequestMdl {
    private String accountNumber;
    private String referenceId;
    private Long amountCents;
    private String pin;
    private String userId;
    private String msisdn;
    private List<AcquirerReferenceMdl> acquirerReference;
}

