package co.za.flash.esb.aggregation.payat.dto.response;

import co.za.flash.esb.aggregation.payat.dto.response.common.LookUpThirdParty;
import co.za.flash.esb.aggregation.payat.dto.response.common.ResponseComDTO;
import lombok.Data;

@Data
public class LookUpResponseDTO extends ResponseComDTO {
    private LookUpThirdParty ThirdPartyMessage;
}
