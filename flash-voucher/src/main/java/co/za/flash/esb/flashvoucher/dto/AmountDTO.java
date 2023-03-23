package co.za.flash.esb.flashvoucher.dto;

import lombok.Data;

@Data
public class AmountDTO {
    private String currency;
    private Long value;
}
