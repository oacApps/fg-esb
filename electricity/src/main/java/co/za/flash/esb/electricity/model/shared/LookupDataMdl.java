package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LookupDataMdl {
    public String requestId;
    public String lookupId;
    public String transactionDate;
    public int amount;
    public BillingInfoMdl billingInfo;
    public MeterInfoMdl meterInfo;
}
