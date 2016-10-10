package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.api.EventsApi;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class EventsRemoteDataSource {
    private static final String TAG = "EventsRemoteDataSource";
    private final EventsApi eventsApi;
    @Inject
    Retrofit retrofit;

    public EventsRemoteDataSource(Context context) {
        MainApplication.getApplicationComponent(context).inject(this);
        Log.d(TAG, "EventsRemoteDataSource: retrofit instance is null? - " + (retrofit == null));
        eventsApi = retrofit.create(EventsApi.class);
    }

    @Nullable
    public List<Event> getEvents() {
        List<Event> events = null;
        try {
            events = eventsApi.getEvents().execute().body().list();
        } catch (IOException e) {
            Log.e(TAG, "getEvents: Error has occured!");
        }
        if (events != null) {
            Log.d(TAG, "getEvents: serialized events - " + events.toString());
        }
        return events;
    }

    @Nullable
    public Event getEvent(@NonNull String id) {
        Event event = null;
        try {
            event = eventsApi.getEvent(id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (event != null) {
            Log.d(TAG, "getEvent: a serialized event - " + event.toString());
        }
        return event;
    }

    @Nullable
    public List<Event> getEventsByLocation(float latitude, float longitude, int distance) {
        List<Event> events = null;
        try {
            events = eventsApi.getEventsByLocation(latitude, longitude, distance).execute().body().list();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (events != null) {
            Log.d(TAG, "getEventsByLocation: a serialized event - " + events.toString());
        }
        return events;
    }

    @Nullable
    public List<Event> getEventsByLocation(float latitude, float longitude) {
        List<Event> events = null;
        if (events != null) {
            Log.d(TAG, "getEventsByLocation: a serialized event - " + events.toString());
        }
        return events;
    }
}