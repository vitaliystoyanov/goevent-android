package com.stoyanov.developer.goevent.utill;

import android.support.annotation.Nullable;
import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

    @Nullable
    public static Date toDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                Locale.getDefault());
        try {
            return date != null ? format.parse(date) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toDuration(Date dateFrom, Date dateTo) {
        SimpleDateFormat fullFormat = new SimpleDateFormat("EEE, MMM d, HH:mm",
                Locale.getDefault());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",
                Locale.getDefault());

        Calendar from = new GregorianCalendar();
        from.setTime(dateFrom);

        Calendar to = new GregorianCalendar();
        to.setTime(dateTo != null ? dateTo : dateFrom);

        if (from.get(Calendar.DAY_OF_MONTH) == to.get(Calendar.DAY_OF_MONTH)
                && from.get(Calendar.MONTH) == to.get(Calendar.MONTH)
                && from.get(Calendar.YEAR) == to.get(Calendar.YEAR)) {
            return fullFormat.format(dateFrom) + " - " + timeFormat.format(dateTo);
        }

        return fullFormat.format(dateFrom) + " - " + fullFormat.format(dateTo);
    }

    public static Pair<String, String> toDurationWithoutTimeRange(Date dateFrom, Date dateTo) {
        SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());

        Calendar from = new GregorianCalendar();
        from.setTime(dateFrom);

        Calendar to = new GregorianCalendar();
        to.setTime(dateTo);
        return new Pair<>(fullFormat.format(dateFrom), fullFormat.format(dateTo));
    }

    public static String toDay(String startTime) {
        Date date = toDate(startTime);
        SimpleDateFormat format = new SimpleDateFormat("dd",
                Locale.getDefault());
        return format.format(date);
    }

    public static String toMonth(String startTime) {
        Date date = toDate(startTime);
        SimpleDateFormat format = new SimpleDateFormat("MMM",
                Locale.getDefault());
        return format.format(date);
    }
}
