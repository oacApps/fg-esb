package co.za.flash.ms.georep.service;

import co.za.flash.ms.georep.entity.LocationsEntity;
import co.za.flash.ms.georep.repo.GeoRepLocationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class GeoRepSrv {

    @Autowired
    private GeoRepLocationsRepo geoRepLocationsRepo;

    public List<LocationsEntity> getLocationsRecords(String dateTimeStr) throws ParseException {
        List<LocationsEntity> sunmiMachineViewList = geoRepLocationsRepo.findByLastActivityContaining(dateTimeStr);
        return sunmiMachineViewList;
    }

}
