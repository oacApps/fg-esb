package co.za.flash.esb.oneforyou.dto.request;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import lombok.Data;

@Data
public class RefundRequestDTO {
    private String requestId;
    private Long amount;
    private String userId;
    private AcquirerDTO acquirer;
}
