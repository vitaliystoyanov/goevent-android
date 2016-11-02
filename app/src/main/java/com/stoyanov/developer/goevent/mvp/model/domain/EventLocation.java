package com.stoyanov.developer.goevent.mvp.model.domain;

import co.uk.rushorm.core.RushObject;

public class EventLocation extends RushObject {
    private Location location;

    public EventLocation() {
    }

    public EventLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
