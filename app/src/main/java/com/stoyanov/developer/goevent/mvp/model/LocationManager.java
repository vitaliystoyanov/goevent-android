package com.stoyanov.developer.goevent.mvp.model;

import com.stoyanov.developer.goevent.mvp.model.domain.LastDefinedLocation;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class LocationManager {

    private static final String TAG = "LocationManager";

    public LastDefinedLocation getLastDefinedLocation() {
        return new RushSearch().findSingle(LastDefinedLocation.class);
    }

    public void updateLastDefinedLocation(LastDefinedLocation location) {
        RushCore.getInstance().deleteAll(LastDefinedLocation.class);
        RushCore.getInstance().save(location);
    }

}
