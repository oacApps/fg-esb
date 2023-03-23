package co.za.flash.esb.electricity.dto.shared;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountDTO {
    @NotBlank(message = "AccountNumber is mandatory")
    private String accountNumber;
    /*@NotBlank(message = "AvailableBalance is mandatory")
    private double availableBalance;
    @NotBlank(message = "Balance is mandatory")
    private double balance;*/
}
