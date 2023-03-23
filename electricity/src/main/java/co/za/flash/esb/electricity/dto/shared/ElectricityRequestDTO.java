package co.za.flash.esb.electricity.dto.shared;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ElectricityRequestDTO {
    @NotNull(message = "requestId is mandatory")
    public  String requestId;
    @NotNull(message = "meterNumber is mandatory")
    public String meterNumber;
    @NotNull(message = "amount is mandatory")
    public double amount;
    @NotNull(message = "acquirer is mandatory")
    public  AcquirerDTO acquirer;



}
