package co.za.flash.esb.cellular.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CellularRequestMdl implements Serializable {
    private Long req_type;
    private String client_id;
    private String password;
    private String reference_id;
    private Long utility;
    private Long network;
    private Long trans_route_type;
    private Long trans_pin_type;
    private String msisdn;
    private Long voucher_type;
    private Long amount;
}
