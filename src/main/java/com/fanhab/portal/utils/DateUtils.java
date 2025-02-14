package com.fanhab.portal.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtils {
    public static Timestamp convertLocalDateToTimestamp(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        LocalDateTime localDateTime = localDate.atStartOfDay();
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp convertLocalDateToTimestamp(LocalDate localDate, ZoneId zoneId) {
        if (localDate == null || zoneId == null) {
            return null;
        }

        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        return Timestamp.from(zonedDateTime.toInstant());
    }
    public static long convertTimestampToLong(Timestamp timestamp) {
        if (timestamp == null) {
            return 0L;
        }

        return timestamp.getTime();
    }
    public static LocalDate convertTimestampToLocalDate(Long timestamp) {
        Date date = new Date(timestamp * 1000);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
