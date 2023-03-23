package co.za.flash.esb.oneforyou.trader.dblookup;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class CredentialLookupDTO {
    private String Organization;
    private String KeyType;
    private String AcquirerId;
    private String AcquirerPassword;
    private String AuthUserName;
    private String AuthPassword;
    private String AccountNumber;
    private boolean Active;
}
