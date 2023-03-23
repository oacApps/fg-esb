package co.za.flash.esb.cellular.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OBResponse {
    private long ResponseCode =-1L;
    private String ReferenceId;
    private String CallCentre;
    private String RechargeResponseType;
    private String ResponseMessage;
    private long NetworkId;
    private long version;
    private String client_id;
    private long SaleAmount;
    private long TransactionID;
    private long TransactionNumber;
    private String DataSize;
}
