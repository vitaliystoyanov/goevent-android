package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface EventsRepository {

    @Nullable
    List<Event> getEvents();

    void setOnNetworkErrorListener(EventsRepositoryImp.OnNotReceiveRemoteListener listener);

    Single<List<Event>> getEventsBy(double latitude, double longitude, boolean updateCache);

    Single<List<Event>> getEventsBy(double latitude, double longitude, String since, String until, boolean updateCache);

    Single<List<Event>> getEventsBy(double latitude, double longitude, int distance, boolean updateCache);

    interface OnNotReceiveRemoteListener {

        void notReceive();

    }
}
