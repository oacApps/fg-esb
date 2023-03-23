package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class UtilityInfoMdl {
    private String name;
    private String address;
    private String vatNumber;
    private String callCentre;
    //private String utilityCallCentre;
    private String partnerCallCentre;
}
