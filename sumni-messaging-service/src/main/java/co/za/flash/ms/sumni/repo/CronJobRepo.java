package co.za.flash.ms.sumni.repo;

import co.za.flash.ms.sumni.entity.CronJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobRepo extends JpaRepository<CronJobEntity, Long> {

}
