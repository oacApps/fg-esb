package co.za.flash.ms.georep.repo;

import co.za.flash.ms.georep.entity.CronJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobRepo extends JpaRepository<CronJobEntity, Long> {

}
