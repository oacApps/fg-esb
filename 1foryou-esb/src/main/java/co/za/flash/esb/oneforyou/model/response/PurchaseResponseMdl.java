package co.za.flash.esb.oneforyou.model.response;

import co.za.flash.esb.oneforyou.model.shared.DataMdl;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseResponseMdl {
    private DataMdl data;
    private int responseCode;
    private String message;
    @JsonProperty
    private boolean iSuccess;
    private List<Object> errors;
}
