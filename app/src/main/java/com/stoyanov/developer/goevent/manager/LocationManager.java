package com.stoyanov.developer.goevent.manager;

import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class LocationManager {

    public LocationPref getLastDefinedLocation() {
        return new RushSearch().findSingle(LocationPref.class);
    }

    public void updateLastDefinedLocation(LocationPref location) {
        RushCore.getInstance().deleteAll(LocationPref.class);
        RushCore.getInstance().save(location);
    }
}
