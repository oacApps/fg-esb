package co.za.flash.esb.electricity.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO {
    private String pin;
    private String serialNumber;
    private String type;
}
