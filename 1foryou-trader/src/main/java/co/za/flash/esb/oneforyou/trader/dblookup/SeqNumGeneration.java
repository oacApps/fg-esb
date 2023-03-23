package co.za.flash.esb.oneforyou.trader.dblookup;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "seqNumGeneration")
@Table(name="NumericSequenceNumberGeneration")
public class SeqNumGeneration {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CreatedDate")
    private String createdDate;

    @Column(name = "ExpiryDate")
    private String expiryDate;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "sequenceNumber")
    private String sequenceNumber;

    @Column(name = "messageType")
    private String messageType;
}
