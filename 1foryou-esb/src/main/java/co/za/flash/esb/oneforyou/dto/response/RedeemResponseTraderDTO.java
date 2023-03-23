package co.za.flash.esb.oneforyou.dto.response;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerRedeemDTO;
import co.za.flash.esb.oneforyou.dto.shared.ChangeVoucherDTO;
import lombok.Data;

@Data
public class RedeemResponseTraderDTO {
    private String requestId;
    private int responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate;
    private Long amount;
    private ChangeVoucherDTO changeVoucher;
    private AcquirerRedeemDTO acquirer;

}
