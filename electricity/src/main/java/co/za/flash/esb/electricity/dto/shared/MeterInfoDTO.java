package co.za.flash.esb.electricity.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeterInfoDTO {
    private String meterNumber;
    private String supplyGrpCode;
    private int keyRevisionNumber;
    private int tokenTechnology;
    private int algorithmCode;
    private String tariffIndex;
    private UtilityInfoDTO utilityInfo;
    private CustomerInfo customer;
}


