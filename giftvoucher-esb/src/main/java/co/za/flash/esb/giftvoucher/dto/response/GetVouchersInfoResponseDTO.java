package co.za.flash.esb.giftvoucher.dto.response;

import co.za.flash.esb.giftvoucher.dto.shared.AnyValueDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetVouchersInfoResponseDTO {
    private String voucherType;
    private String description;
    private int subscription;
    private AnyValueDTO anyvalue;
    private int value;
    private boolean reprint;
    private String voucherTermsOfUseSlip;
    private String voucherTermsOfUseFull;
}
