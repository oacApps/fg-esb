package co.za.flash.esb.aggregation.electricity.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TariffList {
    @JsonSetter("TariffBlock")
    private List<TariffBlock> TariffBlock;

}
