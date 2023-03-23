package co.za.flash.esb.flashvoucher.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDTO {
    private String responseCode;
    private String responseMessage;
    private String transactionID;
    private String responseTime;
    private Long sequenceNumber;
    private String receipt;
    private String accountNumber;
    private List<TokenDTO> tokens;
}
