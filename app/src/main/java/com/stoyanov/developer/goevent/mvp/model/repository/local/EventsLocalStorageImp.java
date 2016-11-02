package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class EventsLocalStorageImp implements EventsStorage {

    @Nullable
    @Override
    public List<Event> getEvents() {
        List<Event> events = new RushSearch().find(Event.class);
        return events;
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {
        RushCore.getInstance().save(events);
    }
}
