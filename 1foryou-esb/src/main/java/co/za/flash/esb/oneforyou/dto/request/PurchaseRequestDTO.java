package co.za.flash.esb.oneforyou.dto.request;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import lombok.Data;

@Data
public class PurchaseRequestDTO {
    private String requestId;
    private int amount;
    private AcquirerDTO acquirer;

}

