package co.za.flash.esb.cellular.database;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "dbo", name = "OneBalanceCredentialLookup")
@Data
public class CredentialLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String Organization;
    private String KeyType;
    private String ClientId;
    private String Password;
    private boolean Active;
    private String ModifiedBy;
    private Date ModifyDate;
    private Date CreateDate;
}
