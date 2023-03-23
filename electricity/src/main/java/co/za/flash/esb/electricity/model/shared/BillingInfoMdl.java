package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

import java.util.List;

@Data
public class BillingInfoMdl {
    private String transactionNumber;
    private String receiptNumber;
    private double amount;
    private int fixedCharges;
    private String unit;
    private double units;
    private int fbe;
    private int daysLastPurchase;
    public double arrearAmount;
    private String arrearDesc;
    private double taxAmount;
    private String customerMessage;
    private String tariffName;
    private List<TariffBlockMdl> tariffBlocks;
    private double refundAmount;
}


