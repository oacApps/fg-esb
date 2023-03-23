package co.za.flash.ms.sumni.repo;

import co.za.flash.ms.sumni.entity.SunmiMachineFailedRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SunmiMachineFailedRecordsRepo extends JpaRepository<SunmiMachineFailedRecordEntity, Long> {
}
