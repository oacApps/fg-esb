package co.za.flash.ms.sumni.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "Cronjob")
public class CronJobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "sumni_last_run_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sumniLastRunDate;
    @Column(name = "georep_last_run_date")
    private Date geoRepLastRunDate;

}

