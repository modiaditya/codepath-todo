package com.aditya.todotasks.utils;

import android.content.Context;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static String getMediumFormatDate(Context context, Date date) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(
            context.getApplicationContext());
        return dateFormat.format(date);
    }

    public static Date getDateFromLong(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        return c.getTime();
    }
}
