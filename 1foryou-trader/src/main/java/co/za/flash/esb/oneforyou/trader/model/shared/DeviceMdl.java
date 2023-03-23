package co.za.flash.esb.oneforyou.trader.model.shared;

import lombok.Data;

@Data
public class DeviceMdl {
    private String imei;
    private String iccid;
    private String msisdn;
    private String imsi;
    private String handsetId;
    private String protocol;
    private String receiptType;
    private String channelID;
    private String platform;
    private String platformVersion;
    private int appType;
    private String appTypeVersion;
    private String appTypeVersionDesc;
    private int latitude;
    private int longitude;
}
