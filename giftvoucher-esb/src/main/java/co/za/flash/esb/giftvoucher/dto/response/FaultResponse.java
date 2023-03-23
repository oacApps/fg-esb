package co.za.flash.esb.giftvoucher.dto.response;

import lombok.Data;

@Data
public class FaultResponse {
    private String type;
    private String message;
    private String description;
}
