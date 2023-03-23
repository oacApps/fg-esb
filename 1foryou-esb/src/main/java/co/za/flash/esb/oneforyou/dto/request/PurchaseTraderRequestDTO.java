package co.za.flash.esb.oneforyou.dto.request;


import co.za.flash.esb.oneforyou.dto.shared.AcquirerTraderDTO;
import co.za.flash.esb.oneforyou.dto.shared.AmountDTO;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseTraderRequestDTO {
    private String accountNumber;
    private Long sequenceNumber;
    private AmountDTO amount;
    private List<AcquirerTraderDTO> acquirerReference;
}

