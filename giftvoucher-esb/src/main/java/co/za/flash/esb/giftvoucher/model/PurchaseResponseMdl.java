package co.za.flash.esb.giftvoucher.model;

import co.za.flash.esb.giftvoucher.model.shared.DataMdl;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseResponseMdl {
    public DataMdl data;
    public int responseCode;
    public String message;
    @JsonProperty
    public boolean isSuccess;
    public List<Object> errors;
}
