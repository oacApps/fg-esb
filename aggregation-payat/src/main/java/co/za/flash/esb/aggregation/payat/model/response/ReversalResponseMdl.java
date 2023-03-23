package co.za.flash.esb.aggregation.payat.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReversalResponseMdl {
    @JsonSetter("OBBillPaymentResponse")
    private OBBillPaymentReversalResponse OBBillPaymentResponse;
    @JsonSetter("OneBalance")
    private OBResponseRefString OneBalance;
}
