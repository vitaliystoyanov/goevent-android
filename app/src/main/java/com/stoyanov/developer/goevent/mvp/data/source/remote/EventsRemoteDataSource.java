package com.stoyanov.developer.goevent.mvp.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.stoyanov.developer.goevent.mvp.data.Event;
import com.stoyanov.developer.goevent.mvp.data.source.EventsDataSource;

import java.util.List;

import javax.inject.Inject;

public class EventsRemoteDataSource implements EventsDataSource {
    private static final String TAG = "EventsRemoteDataSource";
    @Inject Context context;

    public EventsRemoteDataSource() {

    }

    @Nullable
    @Override
    public List<Event> getEvents() {
        Ion.with(context)
                .load("http://localhost:8000/v1.0/events")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d(TAG, "onCompleted: " + result.getAsString(), e);
                    }
                });
        return null;
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {

    }
}
