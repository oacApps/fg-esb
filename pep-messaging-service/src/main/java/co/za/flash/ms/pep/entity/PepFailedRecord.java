package co.za.flash.ms.pep.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PepFailedRecords")
public class PepFailedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="branchNo")
    private String branchNo;

    @Column(name="voucherProductCode")
    private String voucherProductCode;

    @Column(name="cardCode")
    private String cardCode;

    @Column(name="sellDate")
    private String sellDate;
}
