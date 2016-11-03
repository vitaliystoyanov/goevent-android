package com.stoyanov.developer.goevent.utill;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    @Nullable
    public static Date toDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                Locale.getDefault());
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static String toDuration(Date form, Date to) {
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM d, HH:mm",
                Locale.getDefault());
        return format.format(form) + " - " + format.format(to);
    }
}
