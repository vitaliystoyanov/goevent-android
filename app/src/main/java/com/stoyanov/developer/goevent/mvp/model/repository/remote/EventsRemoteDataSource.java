package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventsRemoteDataSource {
    private static final String TAG = "EventsRemoteDataSource";
    private final Context context;
    private UriBuilder uriBuilder;

    public EventsRemoteDataSource(UriBuilder uriBuilder, Context context) {
        this.uriBuilder = uriBuilder;
        this.context = context;
    }

    @Nullable
    public List<Event> getEvents() {
        Log.d(TAG, "getEvents: url is built - " + uriBuilder.getEvents());
        List<Event> events = null;
        try {
            events = Ion.getDefault(context).build(context)
                    .load(uriBuilder.getEvents())
                    .as(new TypeToken<Events>() {
                    })
                    .get().list();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getEvents: ", e);
        }
        if (events != null) {
            Log.d(TAG, "getEvents: serialized events - " + events.toString());
        }
        return events;
    }

    @Nullable
    public Event getEvent(@NonNull String id) {
        Log.d(TAG, "getEvent: url is built - " + uriBuilder.getEvent(id));
        Event event = null;
        try {
            event = Ion.getDefault(context).build(context)
                    .load(uriBuilder.getEvent(id))
                    .as(new TypeToken<Event>() {
                    })
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getEvent: ", e);
        }
        if (event != null) {
            Log.d(TAG, "getEvent: a serialized event - " + event.toString());
        }
        return event;
    }

    @Nullable
    public List<Event> getEventsByLocation(float latitude, float longitude, int distance) {
        Log.d(TAG, "getEventsByLocation: url is built - " +
                uriBuilder.getEventsByLocation(latitude, longitude, distance));
        List<Event> events = null;
        try {
            events = Ion.getDefault(context).build(context)
                    .load(uriBuilder.getEventsByLocation(latitude, longitude, distance))
                    .as(new TypeToken<Events>() {
                    })
                    .get().list();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getEventsByLocation: ", e);
        }
        if (events != null) {
            Log.d(TAG, "getEventsByLocation: a serialized event - " + events.toString());
        }
        return events;
    }

    @Nullable
    public List<Event> getEventsByLocation(float latitude, float longitude) {
        Log.d(TAG, "getEventsByLocation: url is built - " +
                uriBuilder.getEventsByLocation(latitude, longitude));
        List<Event> events = null;
        try {
            events = Ion.getDefault(context).build(context)
                    .load(uriBuilder.getEventsByLocation(latitude, longitude))
                    .as(new TypeToken<Events>() {
                    })
                    .get().list();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getEventsByLocation: ", e);
        }
        if (events != null) {
            Log.d(TAG, "getEventsByLocation: a serialized event - " + events.toString());
        }
        return events;
    }
}