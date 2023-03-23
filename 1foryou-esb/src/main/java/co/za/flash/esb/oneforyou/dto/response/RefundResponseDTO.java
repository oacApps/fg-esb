package co.za.flash.esb.oneforyou.dto.response;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.dto.shared.VoucherDTO;
import lombok.Data;

@Data
public class RefundResponseDTO {
    private String requestId;
    private int responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate;
    private VoucherDTO voucher;
    private AcquirerDTO acquirer;
}
