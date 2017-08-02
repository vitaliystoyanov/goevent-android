package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.api.EventsApi;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Retrofit;

public class EventsServiceImp implements EventsService {
    private static final String TAG = "EventsServiceImp";
    private final EventsApi eventsApi;
    @Inject
    Retrofit retrofit;

    public EventsServiceImp(Context context) {
        GoeventApplication.getApplicationComponent(context).inject(this);
        Log.d(TAG, "EventsServiceImp: retrofit instance is null? - " + (retrofit == null));
        eventsApi = retrofit.create(EventsApi.class);
    }

    @Nullable
    public List<Event> getEvents() {
        Events events = null;
        try {
            events = eventsApi.getEvents().execute().body();
        } catch (IOException e) {
            Log.d(TAG, "getEvents: Error has occurred!", e);
        }
        return events != null ? events.list() : null;
    }

    @Nullable
    public Event getEvent(@NonNull String id) {
        try {
            return eventsApi.getEvent(id).execute().body();
        } catch (IOException e) {
            Log.d(TAG, "getEvents: Error has occurred!", e);
        }
        return null;
    }

    @Nullable
    public Single<Events> getEventsByLocation(double latitude, double longitude, int distance) {
        return eventsApi.getEventsByLocation(latitude, longitude, distance);
    }

    @Nullable
    public Single<Events> getEventsByLocation(double latitude, double longitude) {
        return eventsApi.getEventsByLocation(latitude, longitude, 5000);
    }
}