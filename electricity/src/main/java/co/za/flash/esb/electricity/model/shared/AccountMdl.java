package co.za.flash.esb.electricity.model.shared;

import lombok.Data;

@Data
public class AccountMdl {
    private String accountNumber;
    /*@NotBlank(message = "AvailableBalance is mandatory")
    private double availableBalance;
    @NotBlank(message = "Balance is mandatory")
    private double balance;*/
}
