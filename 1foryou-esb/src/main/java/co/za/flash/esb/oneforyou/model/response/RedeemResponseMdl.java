package co.za.flash.esb.oneforyou.model.response;

import co.za.flash.esb.oneforyou.model.shared.RedeemDataMdl;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RedeemResponseMdl {
    private RedeemDataMdl data;
    private String message;
    private int responseCode;
    private String responseMessage;
    @JsonProperty
    private boolean isSuccess;
    private List<Object> errors;
}
