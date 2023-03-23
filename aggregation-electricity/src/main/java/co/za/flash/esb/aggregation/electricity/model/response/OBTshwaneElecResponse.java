package co.za.flash.esb.aggregation.electricity.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OBTshwaneElecResponse {
    @JsonSetter("ResponseCode")
    private long ResponseCode;
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
    @JsonInclude(valueFilter = PositiveIntegerFilter.class)
    @JsonSetter("TransactionNumber")
    private long TransactionNumber;
    @JsonSetter("Utility")
    private String Utility;
    @JsonSetter("ReferenceId")
    private String ReferenceId;
    @JsonSetter("UtilityName")
    private String UtilityName;
    @JsonSetter("MinAmount")
    private Long MinAmount;
    @JsonSetter("SupplyGrpCode")
    private String SupplyGrpCode;
    @JsonSetter("DaysLastPurchase")
    private Long DaysLastPurchase;
    @JsonSetter("TariffIndex")
    private String TariffIndex;
    @JsonSetter("MeterNumber")
    private Long MeterNumber;
    @JsonSetter("MaxAmount")
    private Long MaxAmount;
    @JsonSetter("KeyRevisionNumber")
    private Long KeyRevisionNumber;
    @JsonSetter("ConsumerAddress")
    private String ConsumerAddress;
    @JsonSetter("TariffName")
    private String TariffName;
    @JsonSetter("VATNumber")
    private Long VATNumber;
    /*@JsonSetter("FixedCharges")
    private float FixedCharges;*/
    @JsonSetter("Unit")
    private String Unit;
    @JsonSetter("FreeUnits")
    private Long FreeUnits;
    @JsonSetter("TaxAmount")
    private float TaxAmount;
    @JsonSetter("ReceiptNumber")
    private String ReceiptNumber;
    @JsonSetter("PaymentDate")
    private String PaymentDate;
    @JsonSetter("Tokens")
    private TokenList Tokens;
    @JsonSetter("TariffBlocks")
    private TariffList TariffBlocks;
    @JsonSetter("TokenTechnology")
    private String TokenTechnology;
    @JsonSetter("UtilityCallCentre")
    private String UtilityCallCentre;
    @JsonSetter("AlgorithmCode")
    private String AlgorithmCode;
    @JsonSetter("CallCentre")
    private String CallCentre;
    @JsonSetter("Amount")
    private float Amount;
    @JsonSetter("Units")
    private float Units;
    @JsonSetter("UtilityAddress")
    private String UtilityAddress;
    @JsonSetter("ConsumerMessage")
    private String ConsumerMessage;
}
