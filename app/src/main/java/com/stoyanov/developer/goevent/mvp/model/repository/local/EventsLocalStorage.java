package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface EventsLocalStorage {

    @Nullable
    List<Event> getEvents();

    void saveEvents(@NonNull List<Event> events);

}
