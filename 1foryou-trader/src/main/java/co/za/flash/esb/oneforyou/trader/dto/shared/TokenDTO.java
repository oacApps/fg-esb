package co.za.flash.esb.oneforyou.trader.dto.shared;

import lombok.Data;

@Data
public class TokenDTO {
    private String pin;
    private String serialNumber;
    private String expiryDate; // "2023-04-28 04:10:14"
    private AmountDTO amount;
}
