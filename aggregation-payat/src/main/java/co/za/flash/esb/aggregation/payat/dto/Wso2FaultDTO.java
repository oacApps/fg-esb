package co.za.flash.esb.aggregation.payat.dto;

import lombok.Data;

@Data
public class Wso2FaultDTO {
    int code;
    String type;
    String message;
    String description;
}
