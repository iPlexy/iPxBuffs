package de.iplexy.iPxBuffs.utils;

import java.time.Instant;

public class TimeUtils {

    public static Long getRemainingTime(Long time){
        return time - System.currentTimeMillis();
    }

    public static String getRemainingTimeFormatted(Long time){
        return formatMilliseconds(getRemainingTime(time));
    }

        public static String formatMilliseconds(long millis) {
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                return days + (days == 1 ? " Tag" : " Tage");
            } else if (hours > 0) {
                return hours + (hours == 1 ? " Stunde" : " Stunden");
            } else if (minutes > 0) {
                return minutes + (minutes == 1 ? " Minute" : " Minuten");
            } else if (seconds > 0) {
                return seconds + (seconds == 1 ? " Sekunde" : " Sekunden");
            } else {
                return millis + (millis == 1 ? " Millisekunde" : " Millisekunden");
            }
        }
}
