package com.stoyanov.developer.goevent;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;

public class LocationPreferences {

    private static final String PREFS_LOCATION = "prefs-location";

    public static void put(LocationPref location) {
        Prefs.putString(PREFS_LOCATION, new Gson().toJson(location));
    }

    public static LocationPref get() {
        return new Gson()
                .fromJson(Prefs.getString(PREFS_LOCATION, ""),
                        LocationPref.class);
    }
}
