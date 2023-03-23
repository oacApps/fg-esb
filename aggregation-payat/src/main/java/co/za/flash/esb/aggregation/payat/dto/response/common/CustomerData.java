package co.za.flash.esb.aggregation.payat.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerData {
    @JsonSetter("FirstName")
    private String FirstName;
    @JsonSetter("LastName")
    private String LastName;
    /*@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = PositiveLongFilter.class)
    @JsonSetter("IdNumber")
    private long IdNumber=-1;*/
    @JsonSetter("ContactNumber")
    private String ContactNumber;
    @JsonSetter("Email")
    private String Email;
}
