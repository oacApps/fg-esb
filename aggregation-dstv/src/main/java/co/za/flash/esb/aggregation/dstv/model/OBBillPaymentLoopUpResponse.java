package co.za.flash.esb.aggregation.dstv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OBBillPaymentLoopUpResponse {
    @JsonSetter("ResponseCode")
    private long ResponseCode;
    @JsonSetter("TransactionNumber")
    private long TransactionNumber;
    @JsonSetter("ConsumerName")
    private String ConsumerName;
    @JsonSetter("ResponseMessage")
    private String ResponseMessage;
    @JsonSetter("version")
    private long version;
    @JsonSetter("NetworkId")
    private long NetworkId;
    @JsonSetter("client_id")
    private String client_id;
    @JsonSetter("SaleAmount")
    private long SaleAmount;
    @JsonSetter("TransactionID")
    private long TransactionID;
    @JsonSetter("AccountNumber")
    private long AccountNumber;
    @JsonSetter("Utility")
    private String Utility;
    @JsonSetter("ReferenceId")
    private String ReferenceId;
    @JsonSetter("VATNumber")
    private String VATNumber;
    @JsonSetter("BalanceAmount")
    private String BalanceAmount;
    @JsonSetter("CallCentre")
    private String CallCentre;
    @JsonSetter("AccountStatus")
    private String AccountStatus;
    @JsonSetter("CustomerNumber")
    private String CustomerNumber;
    @JsonSetter("CellNumber")
    private String CellNumber;
    @JsonSetter("BalanceAmountMaxLimit")
    private String BalanceAmountMaxLimit;
    /*@JsonSetter("ThirdPartyMessage")
    private CompleteThirdParty ThirdPartyMessage;*/
}
