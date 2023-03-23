package co.za.flash.esb.avtrouting.model.dto.request;

import co.za.flash.esb.avtrouting.model.dto.share.AcquirerDTO;
import co.za.flash.esb.avtrouting.model.dto.share.DeviceDTO;
import co.za.flash.esb.avtrouting.model.dto.share.UserDTO;
import lombok.Data;

@Data
public class RedeemRequestDTO {
    private String accountNumber;
    private String sequenceNumber;
    private String voucherPin;
    private String transactionGuid;
    private UserDTO user;
    private AcquirerDTO acquirer;
    private DeviceDTO device;
}
