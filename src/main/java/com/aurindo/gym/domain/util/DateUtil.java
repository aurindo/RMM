package com.aurindo.gym.domain.util;

import com.aurindo.gym.infrastructure.exception.BaseException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final ZoneId ZONE_ID_DEFAULT = ZoneId.of("Europe/London");

    public static ZonedDateTime dateToZonedDateTime(Date date, ZoneId zoneId) throws BaseException {
        if (date == null) {
            throw new BaseException("Date can not be null!");
        }
        return date.toInstant().atZone(zoneId);
    }

    public static ZonedDateTime dateToZonedDateTime(Date date) throws BaseException {
        return dateToZonedDateTime(date, ZONE_ID_DEFAULT);
    }

}
