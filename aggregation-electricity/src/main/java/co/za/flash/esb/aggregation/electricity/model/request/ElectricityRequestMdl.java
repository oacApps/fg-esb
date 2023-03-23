package co.za.flash.esb.aggregation.electricity.model.request;

import lombok.Data;

@Data
public class ElectricityRequestMdl {
    private Long req_type;
    private String client_id;
    private String password;
    private String reference_id;
    private Long utility;
    private Long network;
    private Long trans_route_type;
    private Long trans_pin_type;

    private String meter_id;
    private String storeid;
    private String tillid;
    private Long amount;
    private Long prevend;

    private String key_revision_num;
    private String tariff_index;
    private String supply_group_code;
    private String algorithm_technology;
    private String token_technology;
}
