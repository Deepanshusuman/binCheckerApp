package com.assets.binfinder.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

    public static String getCurrentDateTime(String timeZoneId) {
        // Create a Calendar instance with the specified timezone
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));

        // Get current date and time in the specified timezone
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z z");
        return sdf.format(calendar.getTime());
    }
}
