package co.za.flash.esb.oneforyou.trader.model.request;

import co.za.flash.esb.oneforyou.trader.model.shared.AcquirerMdl;
import co.za.flash.esb.oneforyou.trader.model.shared.DeviceMdl;
import co.za.flash.esb.oneforyou.trader.model.shared.UserMdl;
import lombok.Data;

@Data
public class RefundRedemptionRequestMdl {
    private Long sequenceNumber;
    private DeviceMdl device;
    private UserMdl user;
    private AcquirerMdl acquirer;
    private String redemptionReference;
    private String currency;
    private Long amountRequested;
}
