package co.za.flash.esb.aggregation.payat.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompleteThirdParty {
    @JsonSetter("EchoData")
    private String EchoData;
    @JsonSetter("ResponseCode")
    private String ResponseCode;
    @JsonSetter("Amount")
    private long Amount;
    @JsonSetter("TransmissionDateTime")
    private String TransmissionDateTime;

    @JsonSetter("NetworkTransactionID")
    private Long NetworkTransactionID;
    @JsonSetter("TransactionID")
    private Long TransactionID;

    @JsonSetter("ResponseText")
    private String ResponseText;
    @JsonSetter("DueDate")
    private String DueDate;

    @JsonSetter("MessageType")
    private String MessageType;
    @JsonSetter("MessageID")
    private String MessageID;
    @JsonSetter("CustomerData")
    private CustomerData CustomerData;
    @JsonSetter("StructuredData")
    private StructuredData StructuredData;
    @JsonSetter("IssuerData")
    private IssuerData IssuerData;
    @JsonSetter("PaymentRules")
    private PaymentRules PaymentRules;
}
