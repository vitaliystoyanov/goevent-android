package com.stoyanov.developer.goevent.manager;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.SavedEvent;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoriteEventLocalStorage;

import java.util.ArrayList;
import java.util.List;

public class FavoriteEventManager {
    private FavoriteEventLocalStorage storage;

    public FavoriteEventManager(FavoriteEventLocalStorage storage) {
        this.storage = storage;
    }

    @Nullable
    public List<Event> get() {
        return asListOfEvents(storage.get());
    }

    public void remove(Event event) {
        storage.remove(new SavedEvent(event));
    }

    public void add(Event event) {
        storage.add(new SavedEvent(event));
    }

    public boolean isSaved(Event event) {
        return storage.findSavedEvent(new SavedEvent(event)) != null;
    }

    private List<Event> asListOfEvents(List<SavedEvent> events) {
        List<Event> result = new ArrayList<>();
        for (SavedEvent e : events) {
            result.add(e.toEvent());
        }
        return result;
    }
}
