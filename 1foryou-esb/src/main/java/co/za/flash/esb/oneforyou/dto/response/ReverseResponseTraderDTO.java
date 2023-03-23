package co.za.flash.esb.oneforyou.dto.response;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerRefundTraderDTO;
import lombok.Data;

@Data
public class ReverseResponseTraderDTO {
    private String requestId;
    private int responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate;
    private AcquirerRefundTraderDTO acquirer;
}
