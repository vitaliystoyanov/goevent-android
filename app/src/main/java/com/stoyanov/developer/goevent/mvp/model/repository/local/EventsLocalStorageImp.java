package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public class EventsLocalStorageImp implements EventsLocalStorage {

    @Nullable
    @Override
    public List<Event> getEvents() {
        return Event.listAll(Event.class);
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {
        Event.deleteAll(Event.class);
        for (Event event : events) {
            event.save();
        }
    }
}
