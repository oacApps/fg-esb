package co.za.flash.esb.aggregation.dstv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LookUpResponseMdl {
    @JsonSetter("OBBillPaymentResponse")
    private OBBillPaymentResponse OBBillPaymentResponse;
    @JsonSetter("OneBalance")
    private OBResponseRefString OneBalance;
}
