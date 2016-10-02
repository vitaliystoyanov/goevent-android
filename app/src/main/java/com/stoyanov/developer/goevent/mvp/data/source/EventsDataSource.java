package com.stoyanov.developer.goevent.mvp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.data.Event;

import java.util.List;

public interface EventsDataSource {

    @Nullable List<Event> getEvents();

    void saveEvents(@NonNull List<Event> events);

}
