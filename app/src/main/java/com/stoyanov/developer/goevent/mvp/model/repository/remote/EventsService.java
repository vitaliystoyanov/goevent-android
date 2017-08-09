package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface EventsService {

    @Nullable
    List<Event> getEvents();

    Event getEvent(@NonNull String id);

    Single<Events> getEventsByLocation(double latitude, double longitude, int distance);

    Single<Events> getEventsByLocation(double latitude, double longitude);

    Single<Events> getEventsByLocation(double latitude, double longitude, int maxDistance, String since, String until);

}
