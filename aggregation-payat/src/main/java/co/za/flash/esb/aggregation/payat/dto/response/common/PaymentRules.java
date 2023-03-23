package co.za.flash.esb.aggregation.payat.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRules {
    @JsonSetter("AcceptMultiplesOf")
    private Long AcceptMultiplesOf;
    @JsonSetter("AcceptPartialPayment")
    private boolean AcceptPartialPayment;
    @JsonSetter("AcceptAdditionalPayment")
    private boolean AcceptAdditionalPayment;
    @JsonSetter("MaxAmount")
    private Long MaxAmount;
    @JsonSetter("MinAmount")
    private Long MinAmount;

}
