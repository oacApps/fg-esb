package co.za.flash.ms.sumni.repo;

import co.za.flash.ms.sumni.entity.SunmiMachineViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SunmiMachineViewRepo extends JpaRepository<SunmiMachineViewEntity, String> {

    @Query(value = "select * from vw_sunmi_machine where dateRetrieved >= :dateTime",nativeQuery = true)
    List<SunmiMachineViewEntity> findByDateRetrieved(LocalDateTime dateTime);
}
