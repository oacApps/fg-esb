package co.za.flash.esb.aggregation.electricity.dto.common;

import lombok.Data;

@Data
public class ElectricityRequestDTO {
    private String client_id;
    private String password;
    private String reference_id;
    private String storeid;
    private String tillid;
    private String meter_id;
    private String origin_id;
    private String amount;
}
