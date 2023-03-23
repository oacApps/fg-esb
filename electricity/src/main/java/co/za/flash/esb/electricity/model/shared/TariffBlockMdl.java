package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class TariffBlockMdl {
    private String amount;
    private String costPerUnit;
    private String units;
}
