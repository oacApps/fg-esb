package co.za.flash.esb.cellular.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class AcquirerReferenceDTO implements Serializable {
    private String key;
    private String value;
}
