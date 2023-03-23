package co.za.flash.ms.georep.repo;

import co.za.flash.ms.georep.entity.LocationsFailedRecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoRepFailedRecordsRepo extends JpaRepository<LocationsFailedRecordsEntity, Integer> {

}
