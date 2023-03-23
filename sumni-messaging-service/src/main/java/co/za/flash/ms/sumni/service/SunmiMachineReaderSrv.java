package co.za.flash.ms.sumni.service;

import co.za.flash.ms.sumni.entity.SunmiMachineViewEntity;
import co.za.flash.ms.sumni.repo.SunmiMachineViewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SunmiMachineReaderSrv {

    @Autowired
    private SunmiMachineViewRepo sunmiMachineViewRepo;

    public List<SunmiMachineViewEntity> getSunmiMachineRecords(String dateTimeStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        List<SunmiMachineViewEntity> sunmiMachineViewList = sunmiMachineViewRepo.findByDateRetrieved(dateTime);
        return sunmiMachineViewList;
    }

}
