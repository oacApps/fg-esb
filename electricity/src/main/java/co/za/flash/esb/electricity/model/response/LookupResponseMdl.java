package co.za.flash.esb.electricity.model.response;

import co.za.flash.esb.electricity.model.shared.LookupDataMdl;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LookupResponseMdl {
    private LookupDataMdl data;
    private int responseCode;
    private String message;
    @JsonProperty
    private boolean isSuccess;
    private List<Object> errors;
}
