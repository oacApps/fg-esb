package co.za.flash.credential.management.helper.utils;

import java.sql.Timestamp;

public class TimeUtil {
    public static long millsToMinutes(long mills) {
        // if the time is less than 1 minute, return 1 minute
        long oneMinute = 1 * 60 * 1000;
        if (mills < oneMinute)
            return 1;
        return (mills / oneMinute);
    }

    public static long getTimestampInMills(long originalTimestamp, long addOnInMills) {
        return originalTimestamp + addOnInMills;
    }

    public static long getCurrentTimestampInMills() {
        return System.currentTimeMillis(); //// 2021-03-24 16:48:05.591
    }

    public static Timestamp getCurrentTimestamp() {
        return getTimestamp(getCurrentTimestampInMills());
    }

    public static Timestamp getTimestamp(long timestampInMills) {
        return new Timestamp(timestampInMills);
    }

    public static boolean isTimestampExpired(long timestampInMills, long comparingTimestampInMills) {
        Timestamp timestamp = getTimestamp(timestampInMills);
        Timestamp compareTimestamp = getCurrentTimestamp();
        if (comparingTimestampInMills > 0) {
            compareTimestamp = getTimestamp(comparingTimestampInMills);
        }
        return (timestamp.before(compareTimestamp) || timestamp.equals(compareTimestamp));
    }
}
