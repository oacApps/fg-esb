package co.za.flash.esb.cellular.dto;

import co.za.flash.esb.library.enums.cellular.CellularProductType;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CellularRequestDTO {
    private String network;
    private CellularProductType type;
    private String msisdn;
    private Long amount;
    private String referenceId;

    /*@Hidden
    private String accountNumber;
    @Hidden
    private String sequenceNumber;
    @Hidden
    private List<AcquirerReferenceDTO> acquirerReference;
    @Hidden
    private Long trans_pin_type;
     */
}
