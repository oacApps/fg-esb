package co.za.flash.esb.ricaregistration.dto.response;

import lombok.Data;

@Data
public class SimCard {
    private String iccid;
    private String msisdn;
    private boolean isourstock;
    private String networkInfo;
    private boolean ricad;
}
