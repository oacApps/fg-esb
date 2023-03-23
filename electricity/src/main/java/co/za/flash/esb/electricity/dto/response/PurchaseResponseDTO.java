package co.za.flash.esb.electricity.dto.response;

import co.za.flash.esb.electricity.dto.shared.AcquirerDTO;
import co.za.flash.esb.electricity.dto.shared.BillingInfoDTO;
import co.za.flash.esb.electricity.dto.shared.MeterInfoDTO;
import co.za.flash.esb.electricity.dto.shared.TokenDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseResponseDTO {
    private String requestId;
    private int responseCode;
    private String responseMessage;
    private String transactionId;
    private String transactionDate;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = PositiveIntegerFilter.class)
    private Integer amount;
    private BillingInfoDTO billingInfo; // missing Tariff Info in here
    public MeterInfoDTO meterInfo;
    public List<TokenDTO> tokens;
    public AcquirerDTO acquirer;
}

