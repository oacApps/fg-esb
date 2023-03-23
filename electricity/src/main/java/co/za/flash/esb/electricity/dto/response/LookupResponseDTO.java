package co.za.flash.esb.electricity.dto.response;

import co.za.flash.esb.electricity.dto.shared.AcquirerDTO;
import co.za.flash.esb.electricity.dto.shared.BillingInfoDTO;
import co.za.flash.esb.electricity.dto.shared.MeterInfoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LookupResponseDTO {
    private String requestId; //in ElectricityDTO as well
    private int responseCode;
    private String responseMessage;
    private String lookupId;
    private String transactionDate;


    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = PositiveIntegerFilter.class)
    private Integer amount;
    private BillingInfoDTO billingInfo;
    private MeterInfoDTO meterInfo;
    private AcquirerDTO acquirer; // in ElectricityDTO as well

}
