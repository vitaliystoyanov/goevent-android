package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import java.util.List;

public interface EventsDataSource {

    @Nullable Events getEvents();

    void saveEvents(@NonNull List<Events> events);

}
