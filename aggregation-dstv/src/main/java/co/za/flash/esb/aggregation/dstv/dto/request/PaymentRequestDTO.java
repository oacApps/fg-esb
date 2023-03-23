package co.za.flash.esb.aggregation.dstv.dto.request;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String client_id;
    private String password;
    private String reference_id;
    private String acc_num;
    private String storeid;
    private String tillid;
    private String origin_id;
    private long amount;
}
