package co.za.flash.esb.avtrouting.model.dto.share;

import lombok.Data;

@Data
public class DeviceDTO {
    private String msisdn;
    private String channelId;
    private String platform;
    private String appType;
    private String latitude;
    private String longitude;
}
