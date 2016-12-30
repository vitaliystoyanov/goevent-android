package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;
import java.util.Set;

public interface EventsStorage {

    @Nullable
    List<Event> getEvents();

    @Nullable
    List<Event> getEvents(@NonNull Set<Category> categories);

    void saveEvents(@NonNull List<Event> events);

}
