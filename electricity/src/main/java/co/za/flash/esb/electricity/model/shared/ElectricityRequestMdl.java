package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class ElectricityRequestMdl {
    public  String requestId;
    public String meterNumber;
    public int amount;
    public AcquirerMdl acquirer;
}
