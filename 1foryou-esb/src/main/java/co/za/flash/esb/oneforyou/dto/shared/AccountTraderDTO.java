package co.za.flash.esb.oneforyou.dto.shared;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTraderDTO {
    private Long accountNumber;
    private Long balance;
    private Long availableBalance;
}
