package co.za.flash.esb.oneforyou.trader.dto.request;


import co.za.flash.esb.oneforyou.trader.dto.shared.AmountDTO;
import lombok.Data;

@Data
public class RefundRedemptionRequestDTO {
    private String accountNumber;
    private Long sequenceNumber;
    private String redemptionTransactionId;
    private AmountDTO amount;
    private Object acquirerReference;
}
