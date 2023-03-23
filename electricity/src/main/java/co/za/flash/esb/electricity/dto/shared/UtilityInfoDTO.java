package co.za.flash.esb.electricity.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtilityInfoDTO {
    private String name;
    private String address;
    private String vatNumber;
    private String callCentre;
    private String partnerCallCentre;
}
