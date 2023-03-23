package co.za.flash.esb.oneforyou.trader.dto.request;


import co.za.flash.esb.oneforyou.trader.dto.shared.AmountDTO;
import co.za.flash.esb.oneforyou.trader.dto.shared.CustomerContactDTO;
import lombok.Data;

@Data
public class RedeemRequestDTO {
    private String accountNumber;
    private Long sequenceNumber;
    private String tokenNumber;
    private AmountDTO amount;
    /*private List<AcquirerDTO> acquirerReference;*/
    private Object acquirerReference;
    private CustomerContactDTO customerContact;
    private String referenceId;
}
