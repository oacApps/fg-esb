package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class BlockMdl {
    private double amount;
    private double costPerUnit;
    private double units;
}
