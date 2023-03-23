package co.za.flash.esb.aggregation.dstv.error;

import lombok.Data;

@Data
public class Wso2FaultDTO {
    int code;
    String type;
    String message;
    String description;
}
