package com.franjo.smsapp.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateFormatting {

    public static final String DATE_FORMAT_MESSAGES = "EEEE, MMMM dd YYYY";
    public static final String DATE_FORMAT_MESSAGES_CURRENT_YEAR = "MMM d";
    private static final String DATE_FORMAT_MESSAGES_CURRENT_YEAR_TODAY = "H:m";

    public static final String HEADER_FORMAT_MESSAGES_DETAILS = "EEEE, MMMM d, y";
    public static final String TIME_FORMAT_MESSAGES_DETAILS_ = "HH:mm";

    public static String formatConversationsDate(long dateInMillis, String pattern1, String pattern2) {
        String date;
        DateTimeZone zone = DateTimeZone.getDefault();
        DateTime dateTime = new DateTime(new Date(dateInMillis), zone);
        DateTime now = DateTime.now(zone);
        if (dateTime.getYear() == now.getYear()) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern1);
            date = fmt.print(dateTime);
        } else {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern2);
            date = fmt.print(dateTime);
        }
        return date;
    }

    public static String formatDetailsDate(long dateInMillis, String pattern) {
        String date;
        DateTimeZone zone = DateTimeZone.getDefault();
        DateTime dateTime = new DateTime(new Date(dateInMillis), zone);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        date = fmt.print(dateTime);
        return date;
    }

    public static boolean isSameDate(long previous, long date) {
        DateTimeZone zone = DateTimeZone.getDefault();
        // java.util.Date to Joda-Time, and assign time zone to adjust
        DateTime datePrevious = new DateTime(new Date(previous), zone);
        DateTime dateNext = new DateTime(new Date(date), zone);
        return datePrevious.getYear() == dateNext.getYear() && datePrevious.getMonthOfYear() == dateNext.getMonthOfYear();
    }
}
