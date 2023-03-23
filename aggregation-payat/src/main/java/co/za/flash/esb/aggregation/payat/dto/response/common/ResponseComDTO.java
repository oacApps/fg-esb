package co.za.flash.esb.aggregation.payat.dto.response.common;

import lombok.Data;

@Data
public class ResponseComDTO {
    private String _version;
    private String TransactionID;
    private String ResponseCode;
    private String ResponseMessage;
    private String AccountNumber;
    private String ConsumerName;
    private String ReferenceId;
}
