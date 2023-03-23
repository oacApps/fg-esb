package co.za.flash.ms.pep.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class FlashDates {

    static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String currentDateInString(){
        LocalDate now = LocalDate.now();
        return yyyyMMdd.format(now);
    }

    public static String getNextDay(String givenDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(givenDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        return sdf.format(c.getTime());
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

}
