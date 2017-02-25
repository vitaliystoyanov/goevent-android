package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface EventsRepository {

    @Nullable
    List<Event> getEvents();

//    @Nullable
//    List<Event> getEvents(@NonNull Set<Category> categories);

    void setOnNetworkErrorListener(EventsRepositoryImp.OnNotReceiveRemoteListener listener);

    @Nullable
    List<Event> getEventsEliminateNullLocations();

    @Nullable
    List<Event> getEventsByLocation(double latitude, double longitude);

    interface OnNotReceiveRemoteListener {

        void notReceive();

    }
}
