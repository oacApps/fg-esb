package co.za.flash.esb.electricity.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TariffBlockDTO {
    private String amount;
    private String costPerUnit;
    private String units;
}
