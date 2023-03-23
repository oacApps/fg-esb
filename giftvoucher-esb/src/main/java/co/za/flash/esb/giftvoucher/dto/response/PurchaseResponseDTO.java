package co.za.flash.esb.giftvoucher.dto.response;

import co.za.flash.esb.giftvoucher.dto.shared.VoucherDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class PurchaseResponseDTO {
    private Long transactionID;
    private String sequenceNumber;
    private String userId;
    @JsonInclude(valueFilter = PositiveIntegerFilter.class)
    private int responseCode = -1;
    private String responseTime;
    private String responseMessage = "Unknown problem encountered";
    private VoucherDTO voucher;
    @JsonIgnore
    private String referenceId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<HashMap<String, String>> acquirerReference = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FaultResponse fault;

}
