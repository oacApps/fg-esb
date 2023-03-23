package co.za.flash.ms.sumni.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SumniCronJobFailedRecords")
public class SunmiMachineFailedRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="msn")
    private String msn;

    @Column(name="imei")
    private String imei;
    @Column(name="macAddress")
    private String macAddress;
    @Column(name="machineModel")
    private String machineModel;
    @Column(name="address")
    private String address;
    @Column(name="city")
    private String city;
    @Column(name="province")
    private String province;
    @Column(name="state")
    private String state;
    @Column(name="lat")
    private String lat;
    @Column(name="lng")
    private String lng;
}
