package co.za.flash.esb.oneforyou.trader.dto.request;


import lombok.Data;

@Data
public class ReversalsRedemptionRequestDTO {
    private String accountNumber;
    private Long sequenceNumber;
    private String redemptionTransactionId;
    private Object acquirerReference;
}
