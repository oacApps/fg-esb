package co.za.flash.ms.pep.entity;

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

    @Column(name = "pep_last_run_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pepLastRunDate;

    @Column(name = "shoprite_last_run_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shopRiteLastRunDate;

}

