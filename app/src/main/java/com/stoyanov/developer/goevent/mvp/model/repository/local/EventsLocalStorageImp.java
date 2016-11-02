package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public class EventsLocalStorageImp implements EventsStorage {

    @Nullable
    @Override
    public List<Event> getEvents() {
        return null;
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {

    }
}
