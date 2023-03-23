package co.za.flash.esb.aggregation.electricity.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TariffBlock {
    @JsonSetter("Amount")
    private Float Amount;
    @JsonSetter("CostPerUnit")
    private Float CostPerUnit;
    @JsonSetter("Units")
    private Float Units;
}
