package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import java.util.List;

public interface EventsRepository {

    @Nullable List<Event> getEvents();

    void saveEvents(@NonNull List<Event> events);

}
