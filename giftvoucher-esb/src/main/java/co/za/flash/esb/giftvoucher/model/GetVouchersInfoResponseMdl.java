package co.za.flash.esb.giftvoucher.model;

import co.za.flash.esb.giftvoucher.model.shared.DataMdl;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetVouchersInfoResponseMdl {
    private DataMdl data;
    private int responseCode;
    private String message;
    @JsonProperty
    private boolean isSuccess;
    private List<Object> errors;
}
