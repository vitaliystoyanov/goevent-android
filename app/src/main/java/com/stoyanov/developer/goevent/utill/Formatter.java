package com.stoyanov.developer.goevent.utill;

import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;

public class Formatter {

    public static String formatLocation(LocationPref pref) {
        String result;
        if (pref.getCountry() != null) {
            if (pref.getCity() != null) {
                result = String.format("%s, %s", pref.getCity(), pref.getCountry());
            } else {
                result = String.format("%s", pref.getCountry());
            }
        } else if (pref.getCity() != null) {
            result = String.format("%s", pref.getCountry());
        } else {
            result = String.format("%f, %f", pref.getLatitude(), pref.getLongitude());
        }
        return result;
    }
}
