package co.za.flash.esb.ricaregistration.model.response;

import lombok.Data;

@Data
public class SimCardMdl {
    private String iccid;
    private String msisdn;
    private boolean isourstock;
    private String networkInfo;
    private boolean ricad;
}
