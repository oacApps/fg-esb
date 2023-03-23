package co.za.flash.esb.aggregation.payat.model.response;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompleteResponseMdl{
    @JsonSetter("OBBillPaymentResponse")
    private OBBillPaymentResponse OBBillPaymentResponse;
    @JsonSetter("OneBalance")
    private OBResponseRefString OneBalance;
}
