package co.za.flash.esb.oneforyou.dto.shared;

import lombok.Data;

@Data
public class AcquirerDTO {
    private AccountDTO account;
    private String entityTag;
}
