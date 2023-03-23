package co.za.flash.esb.aggregation.payat.dto.request;

import lombok.Data;

@Data
public class ReversalRequestDTO extends CommonRequestDTO {
    private String cust_ref;
    private String amount;
    private String trader_type;

}
