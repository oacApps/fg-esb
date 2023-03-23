package co.za.flash.esb.oneforyou.model.request;

import co.za.flash.esb.oneforyou.model.shared.AcquirerMdl;
import lombok.Data;

@Data
public class RefundRequestMdl {
    private String requestId;
    private Long amount;
    private String userId;
    private AcquirerMdl acquirer;
}

