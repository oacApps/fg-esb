package co.za.flash.esb.oneforyou.trader.dto.request;

import co.za.flash.esb.oneforyou.trader.dto.shared.AmountDTO;
import lombok.Data;

@Data
public class PurchaseRequestDTO {
    private String accountNumber;
    private Long sequenceNumber;
    private AmountDTO amount;
    private Object acquirerReference;
}
