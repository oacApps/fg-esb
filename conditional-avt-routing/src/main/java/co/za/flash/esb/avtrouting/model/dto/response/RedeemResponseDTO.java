package co.za.flash.esb.avtrouting.model.dto.response;

import co.za.flash.esb.avtrouting.model.dto.share.AmountAuthorisedDTO;
import co.za.flash.esb.avtrouting.model.dto.share.AvailableBalanceDTO;
import co.za.flash.esb.avtrouting.model.dto.share.LedgerBalanceDTO;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedeemResponseDTO {
    private String actionCode;
    private AmountAuthorisedDTO amountAuthorised;
    private AvailableBalanceDTO availableBalance;
    private LedgerBalanceDTO ledgerBalance;
    private String receipt;
    private String errorMessage;
    /*@JsonInclude(JsonInclude.Include.NON_NULL)*/
    private String transactionReference;
}
