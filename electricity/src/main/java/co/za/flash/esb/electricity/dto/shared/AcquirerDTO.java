package co.za.flash.esb.electricity.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcquirerDTO {
    @NotBlank(message = "account is mandatory")
    private AccountDTO account;
    //Not Required
    private String entityTag;
}
