package co.za.flash.esb.cellular.dto;

import lombok.Data;

@Data
public class Wso2FaultDTO {
    String type;
    String message;
    String description;
}
