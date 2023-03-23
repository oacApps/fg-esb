package co.za.flash.esb.electricity.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingInfoDTO {
    private String transactionNumber;
    private String receiptNumber;
    private double amount;
    private int fixedCharges;
    private String unit;
    private double units;
    private int fbe;
    private int daysLastPurchase;
    private double arrearAmount;
    private String arrearDesc;
    private String tariffName;
    private String tariffIndex;
    private double taxAmount;
    private String customerMessage;
    private List<TariffBlockDTO> tariffBlocks;
    private double refundAmount;

}



