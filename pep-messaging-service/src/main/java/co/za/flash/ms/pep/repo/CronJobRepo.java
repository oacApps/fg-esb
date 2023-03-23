package co.za.flash.ms.pep.repo;

import co.za.flash.ms.pep.entity.CronJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobRepo extends JpaRepository<CronJobEntity, Long> {
}
