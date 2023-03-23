package co.za.flash.esb.aggregation.dstv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OBBillPaymentResponse {
    @JsonSetter("ResponseCode")
    private long ResponseCode;
    @JsonSetter("TransactionNumber")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = PositiveLongFilter.class)
    private Long TransactionNumber;
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
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = PositiveLongFilter.class)
    private long AccountNumber =-1;
    @JsonSetter("Utility")
    private String Utility;
    @JsonSetter("ReferenceId")
    private String ReferenceId;
    @JsonSetter("VATNumber")
    private Long VATNumber;
    @JsonSetter("BalanceAmount")
    private Long BalanceAmount;
    @JsonSetter("CallCentre")
    private String CallCentre;
    @JsonSetter("AccountStatus")
    private String AccountStatus;
    @JsonSetter("CustomerNumber")
    private Long CustomerNumber;
    @JsonSetter("CellNumber")
    private Long CellNumber;
    @JsonSetter("BalanceAmountMaxLimit")
    private Long BalanceAmountMaxLimit;
    /*@JsonSetter("ThirdPartyMessage")
    private CompleteThirdParty ThirdPartyMessage;*/
}
