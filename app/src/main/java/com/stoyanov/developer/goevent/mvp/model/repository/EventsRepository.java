package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;
import java.util.Set;

public interface EventsRepository {

    @Nullable List<Event> getEvents();

    @Nullable List<Event> getEvents(@NonNull Set<Category> categories);

    void addOnNetworkErrorListener(EventsRepositoryImp.OnNotReceiveRemoteListener listener);

    List<Event> getEventsEliminateNullLocation();

    interface OnNotReceiveRemoteListener {

        void notReceive();

    }
}
