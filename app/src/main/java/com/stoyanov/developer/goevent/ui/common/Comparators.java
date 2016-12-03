package com.stoyanov.developer.goevent.ui.common;

import android.util.Log;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.utill.DateUtil;

import java.util.Comparator;
import java.util.Date;

public class Comparators {

    private static final String TAG = "Comparators";

    public static class EventsComparatorByStartTime implements Comparator<Event> {
        @Override
        public int compare(Event event, Event t1) {
            Date date1 = DateUtil.toDate(event.getStartTime());
            Date date2 = DateUtil.toDate(t1.getStartTime());
            if (date1 == null || date2 == null) return 0;
            if (date1.before(date2)) {
                return 1;
            } else if (date1.after(date2)) {
                return -1;
            }
            return 0;
        }
    }

    public static class EventsComparatorLocation implements Comparator<Event> {

        @Override
        public int compare(Event event, Event t1) {
            Log.d(TAG, "compare: ");
            Date date1 = DateUtil.toDate(event.getStartTime());
            Date date2 = DateUtil.toDate(t1.getStartTime());
            if (date1 == null || date2 == null) return 0;
            if (date1.after(date2)) {
                return 1;
            } else if (date1.before(date2)) {
                return -1;
            }
            return 0;
        }
    }
}
