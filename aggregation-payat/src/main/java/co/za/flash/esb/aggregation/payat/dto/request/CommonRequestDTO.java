package co.za.flash.esb.aggregation.payat.dto.request;

import lombok.Data;

@Data
public class CommonRequestDTO {
    private String client_id;
    private String password;
    private String reference_id;
    private String acc_num;
    private String origin_id;
    private String storeid;
    private String tillid;
}
