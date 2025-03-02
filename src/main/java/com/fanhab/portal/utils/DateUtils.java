package com.fanhab.portal.utils;

import com.fanhab.portal.exception.ServiceException;

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


    public static void validateDates(LocalDate startDate,LocalDate endDate) {


        if(startDate == null || endDate == null){
            throw ServiceException.badRequestException("تاریخ شروع و پایان نمیتواند خالی باشد");
        }



        if (startDate.isAfter(endDate)) {
            throw ServiceException.badRequestException("تاریخ شروع نمی‌تواند بزرگتر از تاریخ پایان باشد.");
        }


        if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now()) || startDate.isEqual(LocalDate.now())  || endDate.isEqual(LocalDate.now())) {
            throw  ServiceException.badRequestException("تاریخ شروع و پایان نمیتوانند بزرگتر یا برابر تاریخ امروز باشند.");
        }



        int daysBetween = (int) (endDate.toEpochDay() - startDate.toEpochDay());

        if (daysBetween > 31) {
            throw ServiceException.badRequestException("فاصله زمانی بین تاریخ‌ها نمی‌تواند بیش از 31 روز باشد.");
        }


    }

}
