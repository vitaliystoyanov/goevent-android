package com.stoyanov.developer.goevent.mvp.view;

import com.stoyanov.developer.goevent.mvp.model.domain.DefinedLocation;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface NearbyEventsView extends BaseView {

    void showMarkers(List<Event> events);

    void showMessageNetworkError();

    void myLocation();

    void updateMapCamera(DefinedLocation location, boolean isCurrentLocation);

    void visibleProgress(boolean state);
}
