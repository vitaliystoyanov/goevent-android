package com.stoyanov.developer.goevent.mvp.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.data.Event;
import com.stoyanov.developer.goevent.mvp.data.source.EventsDataSource;

import java.util.List;

public class EventsLocalDataSource implements EventsDataSource {

    @Nullable
    @Override
    public List<Event> getEvents() {
        return null;
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {

    }
}
