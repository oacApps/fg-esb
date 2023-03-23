package co.za.flash.esb.oneforyou.trader.dblookup;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class TreasuryCredentialLookupEntity {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "CreateDate")
    private LocalDateTime CreateDate;
    @Column(name = "Organization")
    private String Organization;
    @Column(name = "KeyType")
    private String KeyType;
    @Column(name = "AcquirerId")
    private String AcquirerId;
    @Column(name = "AcquirerPassword")
    private String AcquirerPassword;
    @Column(name = "AuthUserName")
    private String AuthUserName;
    @Column(name = "AuthPassword")
    private String AuthPassword;
    @Column(name = "AccountNumber")
    private String AccountNumber;
    @Column(name = "Active")
    private boolean Active;
    @Column(name = "ModifyDate")
    private LocalDateTime ModifyDate;
    @Column(name = "ModifiedBy")
    private String ModifiedBy;

}
