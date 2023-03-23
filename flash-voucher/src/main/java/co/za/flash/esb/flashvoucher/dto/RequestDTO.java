package co.za.flash.esb.flashvoucher.dto;

import lombok.Data;

@Data
public class RequestDTO {
    private String accountNumber;
    private Long sequenceNumber;
    private AmountDTO amount;
}
