package co.za.flash.esb.oneforyou.dto.request;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.dto.shared.CustomerContactObDTO;
import lombok.Data;

@Data
public class RefundRedemptionDTO {
    private String requestId;
    private Long redemptionTransactionId;
    private Long amount;
    private CustomerContactObDTO customerContact;
    private AcquirerDTO acquirer;
}
