package co.za.flash.esb.aggregation.electricity.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendCoctResponseMdl {
    @JsonSetter("OBElecResponse")
    private OBElecCoctResponse OBElecResponse;
    @JsonSetter("OneBalance")
    private OBResponse OneBalance;
}
