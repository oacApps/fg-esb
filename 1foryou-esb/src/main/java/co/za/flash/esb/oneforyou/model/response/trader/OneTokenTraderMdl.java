package co.za.flash.esb.oneforyou.model.response.trader;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class OneTokenTraderMdl {
    private String pin;
    private String serialNumber;
    private String expiryDate;
    private String expiryDateIso;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extraInfo;
    private int status;
    private Long amount;
}
