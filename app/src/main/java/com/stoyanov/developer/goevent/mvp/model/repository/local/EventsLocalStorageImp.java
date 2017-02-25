package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;
import java.util.Set;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class EventsLocalStorageImp implements EventsLocalStorage {

    @Nullable
    @Override
    public List<Event> getEvents() {
        return new RushSearch().find(Event.class);
    }

    @Nullable
    @Override
    public List<Event> getEvents(@NonNull Set<Category> categories) {
        return new RushSearch().find(Event.class);
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {
        RushCore.getInstance().deleteAll(Event.class);
        RushCore.getInstance().save(events);
    }
}
