package co.za.flash.esb.oneforyou.dto.request;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.dto.shared.CustomerContactDTO;
import lombok.Data;

@Data
public class RedeemRequestDTO {
    private String requestId;
    private String voucherPin;
    private String userId;
    private int amount;
    private CustomerContactDTO customerContact;
    private AcquirerDTO acquirer;
}
