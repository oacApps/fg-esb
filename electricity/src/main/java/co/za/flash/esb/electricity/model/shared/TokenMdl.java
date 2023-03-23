package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class TokenMdl {
    private String pin;
    private String serialNumber;
    private String type;
}
