package co.za.flash.esb.giftvoucher.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnyValueDTO {
    private int min;
    private int max;
}
