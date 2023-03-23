package co.za.flash.esb.aggregation.payat.dto.request;

import lombok.Data;

@Data
public class InitiateRequestDTO extends CommonRequestDTO {
    private String amount;
}
