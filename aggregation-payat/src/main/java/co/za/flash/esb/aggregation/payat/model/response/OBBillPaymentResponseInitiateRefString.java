package co.za.flash.esb.aggregation.payat.model.response;

import co.za.flash.esb.aggregation.payat.dto.response.common.InitiateThirdParty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OBBillPaymentResponseInitiateRefString {
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
    private Long ReferenceId;
    @JsonSetter("ThirdPartyMessage")
    private InitiateThirdParty ThirdPartyMessage;
}
