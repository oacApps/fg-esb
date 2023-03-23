package co.za.flash.esb.electricity.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerInfo {
    private String name;
    private String address;
}
