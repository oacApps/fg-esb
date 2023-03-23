package co.za.flash.esb.oneforyou.dto.response;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.dto.shared.VoucherTraderDTO;
import lombok.Data;

@Data
public class RefundResponseTraderDTO {
    private String requestId;
    private int responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate;
    private VoucherTraderDTO voucher;
    private AcquirerDTO acquirer;
}
