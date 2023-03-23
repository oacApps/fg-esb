package co.za.flash.esb.aggregation.dstv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OBResponseRefString {
    @JsonSetter("ResponseCode")
    private long ResponseCode;
    @JsonSetter("ReferenceId")
    private String ReferenceId;
    @JsonIgnore
    private String CallCentre;
    @JsonIgnore
    private String RechargeResponseType;
    @JsonIgnore
    private String ResponseMessage;
    @JsonSetter("NetworkId")
    private long NetworkId;
    @JsonSetter("version")
    private long version;
    @JsonSetter("client_id")
    private String client_id;
    @JsonSetter("SaleAmount")
    private long SaleAmount;
    @JsonSetter("TransactionID")
    private long TransactionID;
}
