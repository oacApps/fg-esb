package co.za.flash.esb.aggregation.payat.model.request;

import lombok.Data;

@Data
public class PayAtReqMdl {
    private Long req_type;
    private String client_id;
    private String password;
    private String reference_id;
    private String cust_ref;
    private Long utility;
    private Long network;
    private Long trans_route_type;
    private Long trans_pin_type;
    private String acc_num;
    private String storeid;
    private String tillid;
    private Long amount;
    private Long srvc_type;
    private String tender_type;
}
