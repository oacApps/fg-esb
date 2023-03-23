package co.za.flash.esb.oneforyou.model.request;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestMdl {
    private String accountNumber;
    private String referenceId;
    private Long amountCents;
    private String userId;
    private List acquirerReference;
}
