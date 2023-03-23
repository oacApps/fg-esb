package co.za.flash.ms.georep.scheduler;

import co.za.flash.ms.georep.entity.CronJobEntity;
import co.za.flash.ms.georep.entity.LocationsEntity;
import co.za.flash.ms.georep.entity.LocationsFailedRecordsEntity;
import co.za.flash.ms.georep.mapper.LocationsRecordMapper;
import co.za.flash.ms.georep.repo.CronJobRepo;
import co.za.flash.ms.georep.repo.GeoRepFailedRecordsRepo;
import co.za.flash.ms.georep.service.GeoRepSrv;
import co.za.flash.ms.georep.service.RabbitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GeoRepScheduleJob {

    @Autowired

    GeoRepSrv geoRepSrv;

    @Autowired
    GeoRepFailedRecordsRepo geoRepFailedRecordsRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RabbitService rabbitService;

    @Autowired
    CronJobRepo cronJobRepo;

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * <second> <minute> <hour> <day-of-month> <month> <day-of-week>
     *  Every day 02:15 AM
     **/
    @Scheduled(cron = "00 15 02 * * SUN-SAT")
    public void processData() {
        processOldRecords();
        pushNewRecordsToRabbit();
    }

    private void processOldRecords(){
        List<LocationsFailedRecordsEntity> failedRecordEntities = geoRepFailedRecordsRepo.findAll();

        if(!failedRecordEntities.isEmpty()) {
            List<LocationsEntity> locationsEntities = LocationsRecordMapper.INSTANCE.toRecordList(failedRecordEntities);
            boolean isPushSuccess = false;
            try {
                for (LocationsEntity locationsEntity : locationsEntities) {
                    String oldPayloadJson = objectMapper.writeValueAsString(locationsEntity);
                    isPushSuccess = rabbitService.sendMessageToRabbit(oldPayloadJson);
                    if (isPushSuccess) {
                        geoRepFailedRecordsRepo.deleteById(locationsEntity.getLocationId());
                    }
                }
            } catch (JsonProcessingException e) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    private void pushNewRecordsToRabbit(){
        List<LocationsEntity> failedRecordEntities = new ArrayList<>();
        boolean isRabbitPushSuccess = false;
        List<LocationsEntity> locationsEntities = null;
        try{
            locationsEntities = getNewRecordsFromLastRun();
            for (LocationsEntity locationsEntity: locationsEntities) {
                String payloadJson = objectMapper.writeValueAsString(locationsEntity);
                isRabbitPushSuccess = rabbitService.sendMessageToRabbit(payloadJson);
                if(!isRabbitPushSuccess){
                    failedRecordEntities.add(locationsEntity);
                }
            }
        } catch (JsonProcessingException e) {
            LOGGER.info(e.getMessage());
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
        }
        // Save failed records
        if(!failedRecordEntities.isEmpty()){
            saveFailedRecords(failedRecordEntities);
        }
    }

    private List<LocationsEntity> getNewRecordsFromLastRun() throws ParseException {
        String lastRunDateTime =  getLastRunDate();
        List<LocationsEntity> locationsEntities = geoRepSrv.getLocationsRecords(getPreviousDate(lastRunDateTime));
        updateLastRun();
        return locationsEntities;
    }

    private void saveFailedRecords(List<LocationsEntity> failedRecordEntities) {
        List<LocationsFailedRecordsEntity> recordEntities = LocationsRecordMapper.INSTANCE.toFailedRecordList(failedRecordEntities);
        for (LocationsFailedRecordsEntity locationsFailedRecordsEntity: recordEntities) {
            try {
                geoRepFailedRecordsRepo.save(locationsFailedRecordsEntity);
            }catch (Exception e){
                LOGGER.info("Data insertion error: " + e.getLocalizedMessage());
            }
        }
    }

    private void updateLastRun(){
        CronJobEntity cronJobEntity = new CronJobEntity();
        cronJobEntity.setId(1);
        cronJobEntity.setGeoRepLastRunDate(new Date());
        cronJobRepo.save(cronJobEntity);
    }

    private String getPreviousDate(String dateStr){
        LocalDate date = LocalDate.parse(dateStr);
        return date.minusDays(1).toString();
    }

    private String getLastRunDate() {
        Date lastRun;
        List<CronJobEntity> cronJobList = cronJobRepo.findAll();
        if(cronJobList.isEmpty()){
            lastRun = new Date();
            CronJobEntity cronJobEntity = new CronJobEntity();
            cronJobEntity.setId(1);
            cronJobEntity.setGeoRepLastRunDate(lastRun);
            cronJobRepo.save(cronJobEntity);
        } else{
            lastRun = cronJobList.get(0).getGeoRepLastRunDate();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(lastRun);
    }
}
