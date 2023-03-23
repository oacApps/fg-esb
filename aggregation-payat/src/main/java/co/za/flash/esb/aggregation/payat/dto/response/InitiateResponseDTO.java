package co.za.flash.esb.aggregation.payat.dto.response;

import co.za.flash.esb.aggregation.payat.dto.response.common.InitiateThirdParty;
import co.za.flash.esb.aggregation.payat.dto.response.common.LookUpThirdParty;
import co.za.flash.esb.aggregation.payat.dto.response.common.ResponseComDTO;
import lombok.Data;

@Data
public class InitiateResponseDTO extends ResponseComDTO {
    private InitiateThirdParty ThirdPartyMessage;
}
