package co.za.flash.esb.avtrouting.model.dto.share;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmountAuthorisedDTO {
    private String currency;
    private Long value;
}
