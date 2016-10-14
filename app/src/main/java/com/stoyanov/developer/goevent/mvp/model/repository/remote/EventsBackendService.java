package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface EventsBackendService {

    @Nullable
    List<Event> getEvents();

    @Nullable
    Event getEvent(@NonNull String id);

    @Nullable
    List<Event> getEventsByLocation(float latitude, float longitude, int distance);

    @Nullable
    List<Event> getEventsByLocation(float latitude, float longitude);

}
