package com.franjo.smsapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatting {

    public static final String DATE_FORMAT_MESSAGES = "MMM F YYYY";
    private static final String DATE_FORMAT_MESSAGES_CURRENT_YEAR = "MMM F";

    public static final String HEADER_FORMAT_MESSAGES_DETAILS = "EEEE, MMMM d, y";
    public static final String TIME_FORMAT_MESSAGES_DETAILS_ = "hh:mm";

    public static String formatDate (String dateString) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(Long.parseLong(dateString));

        Calendar cal2 = Calendar.getInstance();
        // Current date
        cal2.setTime(new Date());

        // Compare years
        if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            return formattedStringFromMillis(DATE_FORMAT_MESSAGES_CURRENT_YEAR, Long.parseLong(dateString));
        } else {
            return formattedStringFromMillis(DATE_FORMAT_MESSAGES, Long.parseLong(dateString));
        }
    }

    public static String formattedStringFromMillis(String format, long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(cal.getTime());
    }

}
