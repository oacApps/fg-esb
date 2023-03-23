package co.za.flash.esb.oneforyou.trader.model.request;

import co.za.flash.esb.oneforyou.trader.model.shared.*;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestMdl {
    private List<String> transactionPhases;
    private DeviceMdl device;
    private AcquirerMdl acquirer;
    private UserMdl user;
    private AuthenticationMdl authentication;
    private String msisdn;
    private String primaryAccountNumber;
    private Long sequenceNumber;
    private Long amountRequested;
    private Long amountFulfilled;
    private Long fee;
    private String currency;
    private String transactionReference;
    private String voucherReference;
    private DescriptionMdl description;
    private String tranGuid;
    private CardholderMdl cardholder;
    private CardMdl card;
    private String oneTimePin;
    private String notificationMsisdn;
    private String receiverMsisdn;
    private String obTransactionId;
    private String obResponseCode;
    private String obResponseMessage;
}
