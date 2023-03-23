package co.za.flash.ms.georep.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cron_job_geo_rep")
public class CronJobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "georep_last_run_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date geoRepLastRunDate;

}

