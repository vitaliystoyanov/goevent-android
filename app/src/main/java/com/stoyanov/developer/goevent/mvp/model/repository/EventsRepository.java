package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface EventsRepository {

    @Nullable List<Event> getEvents();

    void addOnNetworkErrorListener(EventsRepositoryImp.OnNotReceiveRemoteListener listener);

    List<Event> getEventsEliminateNullLocation();

    interface OnNotReceiveRemoteListener {

        void notReceive();

    }
}
