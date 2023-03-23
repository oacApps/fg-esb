package co.za.flash.ms.sumni.scheduler;

import co.za.flash.ms.sumni.entity.CronJobEntity;
import co.za.flash.ms.sumni.entity.SunmiMachineFailedRecordEntity;
import co.za.flash.ms.sumni.entity.SunmiMachineViewEntity;
import co.za.flash.ms.sumni.mapper.SunmiMachineRecordMapper;
import co.za.flash.ms.sumni.repo.CronJobRepo;
import co.za.flash.ms.sumni.repo.SunmiMachineFailedRecordsRepo;
import co.za.flash.ms.sumni.service.RabbitService;
import co.za.flash.ms.sumni.service.SunmiMachineReaderSrv;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SumniScheduleJob {

    @Autowired
    SunmiMachineReaderSrv sunmiMachineReaderSrv;

    @Autowired
    SunmiMachineFailedRecordsRepo sunmiMachineFailedRecordsRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RabbitService rabbitService;

    @Autowired
    CronJobRepo cronJobRepo;

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * <second> <minute> <hour> <day-of-month> <month> <day-of-week>
     *  Every day 03:15 AM
     **/
    @Scheduled(cron = "00 15 03 * * SUN-SAT")
    public void processData() {
        processOldRecords();
        pushNewRecordsToRabbit();
    }

    private void processOldRecords(){
        List<SunmiMachineFailedRecordEntity> failedRecordEntities = sunmiMachineFailedRecordsRepo.findAll();

        if(!failedRecordEntities.isEmpty()) {
            List<SunmiMachineViewEntity> machineViewEntities = SunmiMachineRecordMapper.INSTANCE.toRecordList(failedRecordEntities);
            boolean isPushSuccess = false;
            try {
                for (SunmiMachineViewEntity sunmiMachineViewEntity : machineViewEntities) {
                    String oldPayloadJson = objectMapper.writeValueAsString(sunmiMachineViewEntity);
                    isPushSuccess = rabbitService.sendMessageToRabbit(oldPayloadJson);
                    if (isPushSuccess) {
                        sunmiMachineFailedRecordsRepo.deleteById(sunmiMachineViewEntity.getId());
                    }
                }
            } catch (JsonProcessingException e) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    private void pushNewRecordsToRabbit(){
        List<SunmiMachineViewEntity> failedRecordEntities = new ArrayList<>();
        boolean isRabbitPushSuccess = false;
        List<SunmiMachineViewEntity> sunmiMachineViewList = getNewRecordsFromLastRun();
        try{
            for (SunmiMachineViewEntity sunmiMachineViewEntity: sunmiMachineViewList) {
                String payloadJson = objectMapper.writeValueAsString(sunmiMachineViewEntity);
                isRabbitPushSuccess = rabbitService.sendMessageToRabbit(payloadJson);
                if(!isRabbitPushSuccess){
                    failedRecordEntities.add(sunmiMachineViewEntity);
                }
            }
        } catch (JsonProcessingException e) {
            LOGGER.info(e.getMessage());
        }
        // Save failed records
        if(!failedRecordEntities.isEmpty()){
            saveFailedRecords(failedRecordEntities);
        }
    }

    private List<SunmiMachineViewEntity> getNewRecordsFromLastRun(){
        String lastRunDateTime =  getLastRunDate();
        List<SunmiMachineViewEntity> sunmiMachineViewEntities = sunmiMachineReaderSrv.getSunmiMachineRecords(lastRunDateTime);
        updateLastRun();
        return sunmiMachineViewEntities;
    }

    private void saveFailedRecords(List<SunmiMachineViewEntity> failedRecordEntities) {
        List<SunmiMachineFailedRecordEntity> recordEntities = SunmiMachineRecordMapper.INSTANCE.toFailedRecordList(failedRecordEntities);
        for (SunmiMachineFailedRecordEntity sunmiMachineFailedRecordEntity: recordEntities) {
            sunmiMachineFailedRecordsRepo.save(sunmiMachineFailedRecordEntity);
        }
    }

    private void updateLastRun(){
        CronJobEntity cronJobEntity = new CronJobEntity();
        cronJobEntity.setId(1);
        cronJobEntity.setSumniLastRunDate(new Date());
        cronJobRepo.save(cronJobEntity);
    }

    private String getLastRunDate() {
        Date lastRun;
        List<CronJobEntity> cronJobList = cronJobRepo.findAll();
        if(cronJobList.isEmpty()){
            lastRun = new Date();
            CronJobEntity cronJobEntity = new CronJobEntity();
            cronJobEntity.setId(1);
            cronJobEntity.setSumniLastRunDate(lastRun);
            cronJobRepo.save(cronJobEntity);
        } else{
            lastRun = cronJobList.get(0).getSumniLastRunDate();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return newFormat.format(lastRun);
    }
}
