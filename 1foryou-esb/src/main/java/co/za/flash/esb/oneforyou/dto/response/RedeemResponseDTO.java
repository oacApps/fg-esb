package co.za.flash.esb.oneforyou.dto.response;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.dto.shared.ChangeVoucherDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedeemResponseDTO {
    private String requestId;
    private int responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate;
    private Long amount;
    private ChangeVoucherDTO changeVoucher;
    private AcquirerDTO acquirer;

}
