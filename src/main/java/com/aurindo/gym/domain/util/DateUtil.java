package com.aurindo.gym.domain.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final ZoneId ZONE_ID_DEFAULT = ZoneId.of("Europe/London");

    public static ZonedDateTime dateToZonedDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId);
    }

    public static ZonedDateTime dateToZonedDateTime(Date date) {
        return dateToZonedDateTime(date, ZONE_ID_DEFAULT);
    }

}
