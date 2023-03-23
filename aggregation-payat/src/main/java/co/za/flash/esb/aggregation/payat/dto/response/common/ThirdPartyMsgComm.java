package co.za.flash.esb.aggregation.payat.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThirdPartyMsgComm {
    private String ResponseCode;
    private String ResponseText;
    private String DueDate;
    private String EchoData;
    private String MessageType;
    private String MessageID;
    private String TransmissionDateTime;
    private String NetworkTransactionID;
    private String TransactionID;
    private String Amount;
}
