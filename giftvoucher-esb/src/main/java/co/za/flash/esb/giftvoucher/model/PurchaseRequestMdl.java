package co.za.flash.esb.giftvoucher.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Backend REQUEST to AggregationTreasury
//No sequence Number
@Data
public class PurchaseRequestMdl implements Serializable {
    private String accountNumber;
    private String referenceId;
    private int amountCents;
    private String userId;
    private String vendor; //voucherType
    //private List<AcquirerReferenceMdl> acquirerReference;
    List<HashMap<String, String>> acquirerReference = new ArrayList<>();

}
