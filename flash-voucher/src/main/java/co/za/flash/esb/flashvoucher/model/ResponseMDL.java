package co.za.flash.esb.flashvoucher.model;

import lombok.Data;

@Data
public class ResponseMDL {
    private String actionCode;
    private String screenMessage;
    private Long sequenceNumber;
  //  private String receipt;
   // private Long amountAuthorised;
    private Long balance;
    private Long availableBalance;
  //  private String currency;
    private String transactionReference;
  //  private String transactionDate;
    private CashVoucherMDL cashVoucher;
}
