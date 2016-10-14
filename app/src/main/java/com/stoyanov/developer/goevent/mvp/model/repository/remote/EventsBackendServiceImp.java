package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.api.EventsApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class EventsBackendServiceImp implements EventsBackendService {
    private static final String TAG = "EventsBackendServiceImp";
    private final EventsApi eventsApi;
    @Inject
    Retrofit retrofit;

    public EventsBackendServiceImp(Context context) {
        MainApplication.getApplicationComponent(context).inject(this);
        Log.d(TAG, "EventsBackendServiceImp: retrofit instance is null? - " + (retrofit == null));
        eventsApi = retrofit.create(EventsApi.class);
    }

    @Nullable
    public List<Event> getEvents() {
        try {
            Events body = eventsApi.getEvents().execute().body();
            if (body == null) return null;
            return new ArrayList<>(body.list());
        } catch (IOException e) {
            Log.d(TAG, "getEvents: Error has occurred!", e);
        }
        return null;
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
    public List<Event> getEventsByLocation(float latitude, float longitude, int distance) {
        try {
            Events responseBody = eventsApi.getEventsByLocation(latitude, longitude, distance)
                    .execute()
                    .body();
            if (responseBody == null) return null;
            return responseBody.list();
        } catch (IOException e) {
            Log.d(TAG, "getEvents: Error has occurred!", e);
        }
        return null;
    }

    @Nullable
    public List<Event> getEventsByLocation(float latitude, float longitude) {
        try {
            Events body = eventsApi.getEventsByLocation(latitude, longitude).execute().body();
            if (body == null) return null;
            return body.list();
        } catch (IOException e) {
            Log.d(TAG, "getEvents: Error has occurred!", e);
        }
        return null;
    }
}