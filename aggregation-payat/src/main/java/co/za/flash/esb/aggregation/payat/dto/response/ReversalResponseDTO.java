package co.za.flash.esb.aggregation.payat.dto.response;

import co.za.flash.esb.aggregation.payat.dto.response.common.CompleteThirdParty;
import co.za.flash.esb.aggregation.payat.dto.response.common.ResponseComDTO;
import co.za.flash.esb.aggregation.payat.dto.response.common.ReversalThirdParty;
import lombok.Data;

@Data
public class ReversalResponseDTO {
    private String _version;
    private String TransactionID;
    private String ResponseCode;
    private String ResponseMessage;
    private String AccountNumber;
    private String ReferenceId;
    private ReversalThirdParty ThirdPartyMessage;
}
