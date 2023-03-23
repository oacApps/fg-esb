package co.za.flash.esb.aggregation.payat.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssuerData {
    @JsonSetter("Name")
    private String Name;
    @JsonSetter("ContactNumber")
    private String ContactNumber;
}
