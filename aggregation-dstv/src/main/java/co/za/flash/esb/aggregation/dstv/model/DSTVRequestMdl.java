package co.za.flash.esb.aggregation.dstv.model;

import lombok.Data;

@Data
public class DSTVRequestMdl {
    private Long req_type;
    private String client_id;
    private String password;
    private String reference_id;
    private Long utility;
    private Long network;
    private Long trans_route_type;
    private Long trans_pin_type;
    private String acc_num;
    private String storeid;
    private String tillid;
    private Long amount;
}
