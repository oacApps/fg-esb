package co.za.flash.ms.georep.repo;

import co.za.flash.ms.georep.entity.LocationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeoRepLocationsRepo extends JpaRepository<LocationsEntity, Integer> {

    //@Query(value = "select * from locations where last_activity like '%:dateStr%'",nativeQuery = true)
    List<LocationsEntity> findByLastActivityContaining(String dateStr);
}
