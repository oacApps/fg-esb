package co.za.flash.ms.sumni.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "vw_sunmi_machine")
public class SunmiMachineViewEntity {
    @Id
    @Column(name="msn")
    private String msn;

    @Transient
    private Long id;

    @Column(name="imei")
    private String imei;
    @Column(name="macAddress")
    private String macAddress;
    @Column(name="machineModel")
    private String machineModel;

    @Column(name="dateRetrieved")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dateRetrieved;

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
