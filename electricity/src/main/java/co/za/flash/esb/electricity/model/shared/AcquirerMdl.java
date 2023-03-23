package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class AcquirerMdl {
    private AccountMdl account;
    //Not Required
    private String entityTag;
}
