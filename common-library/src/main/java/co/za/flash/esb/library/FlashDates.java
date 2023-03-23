package co.za.flash.esb.library;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlashDates {

    static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    static DateTimeFormatter flashStandard = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    public static String stringDateTimeToStringDateTime(String dateTime){

        return dateTime.replace("T", " ");
/*        Date date = null;
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));
        try {
            date = utcFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formatEdDateTime = String.valueOf(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));

        return formatEdDateTime;*/
    }
    public static String currentDateTimeFlashFormatInString(){
        LocalDateTime now = LocalDateTime.now();
        return flashStandard.format(now);
    }

    public static String currentDateInString(){
        LocalDate now = LocalDate.now();
        return yyyyMMdd.format(now);
    }

    public static String yesterdayDateInString(){
        LocalDate now = LocalDate.now().minusDays(1L);
        return yyyyMMdd.format(now);
    }
    public static String noOfDaysAgoDateInString(long daysToSubtract){
        LocalDate now = LocalDate.now().minusDays(daysToSubtract);
        return yyyyMMdd.format(now);
    }

    public static int dayOfTheWeek(){
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    public static Long differenceOfLocalDateTimeInMillis(LocalDateTime startDateTime, LocalDateTime endDateTime){
        Duration duration = Duration .between(startDateTime, endDateTime);
        return duration.toMillis();
    }

}
