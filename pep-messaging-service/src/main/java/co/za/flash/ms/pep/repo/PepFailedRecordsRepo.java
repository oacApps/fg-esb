package co.za.flash.ms.pep.repo;

import co.za.flash.ms.pep.entity.PepFailedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PepFailedRecordsRepo extends JpaRepository<PepFailedRecord, Long> {
}
