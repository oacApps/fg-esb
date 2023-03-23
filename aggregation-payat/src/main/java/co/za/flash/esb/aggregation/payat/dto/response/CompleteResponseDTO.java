package co.za.flash.esb.aggregation.payat.dto.response;

import co.za.flash.esb.aggregation.payat.dto.response.common.CompleteThirdParty;
import co.za.flash.esb.aggregation.payat.dto.response.common.InitiateThirdParty;
import co.za.flash.esb.aggregation.payat.dto.response.common.ResponseComDTO;
import lombok.Data;

@Data
public class CompleteResponseDTO extends ResponseComDTO {
    private CompleteThirdParty ThirdPartyMessage;
}
