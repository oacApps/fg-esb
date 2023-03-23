package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseDataMdl {
    private String requestId;
    private String transactionId;
    private String transactionDate;
    private int amount;
    private BillingInfoMdl billingInfo;
    private MeterInfoMdl meterInfo;
    private List<TokenMdl> tokens;
}
