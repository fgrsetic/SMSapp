package com.franjo.smsapp.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateFormatting {

    public static final String DATE_FORMAT_MESSAGES = "MMM d YYYY";
    private static final String DATE_FORMAT_MESSAGES_CURRENT_YEAR = "MMM d";
    private static final String DATE_FORMAT_MESSAGES_CURRENT_YEAR_TODAY = "H:m";

    public static final String HEADER_FORMAT_MESSAGES_DETAILS = "EEEE, MMMM d, y";
    public static final String TIME_FORMAT_MESSAGES_DETAILS_ = "hh:mm";

    public static String formatDate(long dateInMillis) {
        String date;
        DateTimeZone zone = DateTimeZone.getDefault();
        // java.util.Date to Joda-Time, and assign time zone to adjust
        DateTime dateTime = new DateTime(new Date(dateInMillis), zone);
        DateTime now = DateTime.now(zone);
        if (dateTime.getYear() == now.getYear()) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT_MESSAGES_CURRENT_YEAR);
            date = fmt.print(dateTime);
        } else {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT_MESSAGES);
            date = fmt.print(dateTime);
        }
        return date;
    }
}
