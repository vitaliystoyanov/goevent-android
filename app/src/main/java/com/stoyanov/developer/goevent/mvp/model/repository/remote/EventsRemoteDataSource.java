package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;

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
    public Events getEvents() {
        Events result = null;
        try {
            Log.d(TAG, "getEvents: url is built - " + uriBuilder.getEvents());
            result = Ion.getDefault(context).build(context)
                    .load(uriBuilder.getEvents())
                    .as(new TypeToken<Events>() {})
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "getEvents: ", e);
        }
        if (result != null) {
            Log.d(TAG, "getEvents: serialized events - " + result.toString());
        }
        return result;
    }

    @Nullable
    public Event getEvent(@NonNull String id) {
        Event event = null;
        try {
            Log.d(TAG, "getEvent: url is built - " + uriBuilder.getEvent(id));
            event = Ion.getDefault(context).build(context)
                    .load(uriBuilder.getEvent(id))
                    .as(new TypeToken<Event>() {})
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "getEvent: ", e);
        }
        if (event != null) {
            Log.d(TAG, "getEvent: a serialized event - " + event.toString());
        }
        return event;
    }

    @Nullable
    public Events getEventsByLocation(float latitude, float longitude, int distance) {
        return null;
    }
}