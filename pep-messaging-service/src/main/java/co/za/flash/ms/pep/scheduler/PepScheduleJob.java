package co.za.flash.ms.pep.scheduler;

import co.za.flash.ms.pep.entity.CronJobEntity;
import co.za.flash.ms.pep.entity.PepFailedRecord;
import co.za.flash.ms.pep.mapper.PepDataMapper;
import co.za.flash.ms.pep.model.PepData;
import co.za.flash.ms.pep.repo.CronJobRepo;
import co.za.flash.ms.pep.repo.PepFailedRecordsRepo;
import co.za.flash.ms.pep.services.RabbitService;
import co.za.flash.ms.pep.utils.FlashDates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PepScheduleJob extends SftpFileSystemService {

    Logger LOGGER = LoggerFactory.getLogger(PepScheduleJob.class);

    private String pepFileName="vch_all_sold_daily_FLASH_";
    private String pepFileExtensionCSV=".csv";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RabbitService rabbitService;

    @Autowired
    CronJobRepo cronJobRepo;

    @Autowired
    PepFailedRecordsRepo pepFailedRecordsRepo;

    /*<second> <minute> <hour> <day-of-month> <month> <day-of-week>*/
    /*@Scheduled(cron = "00 05 22 * * MON-FRI")*/
    @Scheduled(cron = "*/1 * * * * *")
    public void runPepScheduleJob() throws Exception {
        processOldRecords();
        pushNewRecordsToRabbit();
    }

    private void processOldRecords(){
        LOGGER.info("processing Old Records");
        List<PepFailedRecord> failedRecordEntities = pepFailedRecordsRepo.findAll();

        if(!failedRecordEntities.isEmpty()) {
            boolean isPushSuccess = false;
            try {
                for (PepFailedRecord pepFailedRecord : failedRecordEntities) {
                    PepData pepData = PepDataMapper.INSTANCE.toMDL(pepFailedRecord);
                    String oldPayloadJson = objectMapper.writeValueAsString(pepData);
                    isPushSuccess = rabbitService.sendMessageToRabbit(oldPayloadJson);
                    if (isPushSuccess) {
                        pepFailedRecordsRepo.deleteById(pepFailedRecord.getId());
                    }
                }
            } catch (JsonProcessingException e) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    private void pushNewRecordsToRabbit() throws Exception {
        LOGGER.info("processing new Records");
        List<PepData> pepFailedRecords = new ArrayList<>();

        String fileName = pepDataFileName();
        try {
            BufferedReader fileReader = downloadFileContent(config.getPepRoot(), fileName);
            String line ;
            boolean isRabbitPushSuccess = false;
            while ((line = fileReader.readLine()) != null) {
                String[] values = line.split(",");
                if(values.length == 4) {
                    PepData pepData = new PepData(values[0], values[1], values[2], values[3]);
                    String payloadJson = objectMapper.writeValueAsString(pepData);
                    isRabbitPushSuccess = rabbitService.sendMessageToRabbit(payloadJson);
                    if(!isRabbitPushSuccess){
                        pepFailedRecords.add(pepData);
                    }
                }
            }
            updateLastRun();
            fileReader.close();
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Download file failure. TargetPath: {}", fileName, e);
            throw new Exception("Download File failure");
        }

        // Save failed records
        if(!pepFailedRecords.isEmpty()){
            saveFailedRecords(pepFailedRecords);
        }
    }

    private void updateLastRun(){
        CronJobEntity cronJobEntity = new CronJobEntity();
        cronJobEntity.setId(1);
        cronJobEntity.setPepLastRunDate(new Date());
        cronJobRepo.save(cronJobEntity);
    }

    private String getLastRunDate() {
        Date lastRun;
        List<CronJobEntity> cronJobList = cronJobRepo.findAll();
        if(cronJobList.isEmpty()){
            lastRun = new Date();
            CronJobEntity cronJobEntity = new CronJobEntity();
            cronJobEntity.setId(1);
            cronJobEntity.setPepLastRunDate(lastRun);
            cronJobRepo.save(cronJobEntity);
        } else{
            lastRun = cronJobList.get(0).getPepLastRunDate();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyyMMdd");
        return newFormat.format(lastRun);
    }

    private void saveFailedRecords(List<PepData> pepFailedRecordList) {
        List<PepFailedRecord> pepFailedRecords = PepDataMapper.INSTANCE.toEntityList(pepFailedRecordList);

        for (PepFailedRecord pepFailedRecord: pepFailedRecords) {
            pepFailedRecordsRepo.save(pepFailedRecord);
        }
    }

    private String pepDataFileName(){
       /* String startDate = FlashDates.dayOfTheWeek() == 1 ?
                FlashDates.noOfDaysAgoDateInString(2) :
                FlashDates.yesterdayDateInString();*/

        String lastRunDateTime =  getLastRunDate();
        String startDate = lastRunDateTime;
        String nextDate = FlashDates.getNextDay(startDate);

        /** File name format : vch_all_sold_daily_FLASH_20210226-20210227 **/
        String fileName= new StringBuilder(pepFileName)
                .append(startDate)
                .append("-")
                .append(nextDate)
                .append(pepFileExtensionCSV)
                .toString();
        return fileName;
    }


}
