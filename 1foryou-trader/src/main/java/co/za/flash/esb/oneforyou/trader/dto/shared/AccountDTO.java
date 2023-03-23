package co.za.flash.esb.oneforyou.trader.dto.shared;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    private String accountNumber;
    private String balance;
    private String availableBalance;
}
