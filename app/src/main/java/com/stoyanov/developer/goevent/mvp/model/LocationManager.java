package com.stoyanov.developer.goevent.mvp.model;

import com.stoyanov.developer.goevent.mvp.model.domain.DefinedLocation;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class LocationManager {

    public DefinedLocation getLastDefinedLocation() {
        return new RushSearch().findSingle(DefinedLocation.class);
    }

    public void updateLastDefinedLocation(DefinedLocation location) {
        RushCore.getInstance().deleteAll(DefinedLocation.class);
        RushCore.getInstance().save(location);
    }

}
