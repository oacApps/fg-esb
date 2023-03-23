package co.za.flash.esb.avtrouting.model.dto;

import lombok.Data;

@Data
public class Errors {
    private int responseCode;
    private ErrorTmp error;
}
