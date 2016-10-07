package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Events;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsDataSource;

import java.util.List;

public class EventsLocalDataSource implements EventsDataSource {

    @Nullable
    @Override
    public Events getEvents() {
        return null;
    }

    @Override
    public void saveEvents(@NonNull List<Events> events) {

    }
}
