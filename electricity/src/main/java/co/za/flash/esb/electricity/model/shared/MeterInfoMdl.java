package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class MeterInfoMdl {
    private String meterNumber;
    private String supplyGrpCode;
    private int keyRevisionNumber;
    private int tokenTechnology;
    private int algorithmCode;
    private String tariffIndex;
    private UtilityInfoMdl utilityInfo;
    private ConsumerInfoMdl consumerInfo;
}
