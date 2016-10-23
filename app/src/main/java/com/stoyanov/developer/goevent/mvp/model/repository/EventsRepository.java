package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoritesEventsStorage;

import java.util.List;

public interface EventsRepository extends FavoritesEventsStorage {

    @Nullable List<Event> getEvents();

    void saveEvents(@NonNull List<Event> events);

    void addOnNotReceiveRemoteListener(EventsRepositoryImp.OnNotReceiveRemoteListener listener);

    interface OnNotReceiveRemoteListener {

        void notReceive();

    }
}
