package co.za.flash.esb.giftvoucher.dto.request;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class PurchaseRequestDTO {
    private  String referenceId;
    @NotNull
    private int amount;
    //enum voucherType
    private String voucherType;
    private String userId;
    @NotNull
    private  String accountNumber;
    private  String sequenceNumber;
   // private List<AcquirerReferenceDTO> acquirerReference;
    List<HashMap<String, String>> acquirerReference = new ArrayList<>();
}
