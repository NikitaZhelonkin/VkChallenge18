package com.vk.challenge.utils;

import android.content.Context;

import com.vk.challenge.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DISPLAY_DATE_FORMAT_THIS_YEAR = "dd MMMM";
    private static final String DISPLAY_DATE_TIME_FORMAT = "HH:mm, dd.MM.yy";
    private static final String DISPLAY_TIME_FORMAT = "HH:mm";

    public static String format(long timeMillis) {
        return format(timeMillis, DATE_TIME_FORMAT);
    }

    private static String format(long timeMillis, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(timeMillis);
    }

    public static String formatRelativeSeconds(Context context, long timeMillis){
        return formatRelative(context, TimeUnit.SECONDS.toMillis(timeMillis));
    }

    public static String formatRelative(Context context, long timeMillis) {
        if (timeMillis == 0) {
            return null;
        }
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeMillis);
        if (today.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
            int daysAgo = today.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR);
            String dateString;
            if (daysAgo == 0) {
                dateString = context.getString(R.string.today);
            } else if (daysAgo == 1) {
                dateString = context.getString(R.string.yesterday);
            } else if (daysAgo < 7) {
                dateString = context.getResources().getQuantityString(R.plurals.days_back, daysAgo, daysAgo);
            } else {
                dateString = format(timeMillis, DISPLAY_DATE_FORMAT_THIS_YEAR);
            }
            return context.getString(R.string.date_time, dateString, format(timeMillis, DISPLAY_TIME_FORMAT));
        } else {
            return format(timeMillis, DISPLAY_DATE_TIME_FORMAT);
        }
    }
}
