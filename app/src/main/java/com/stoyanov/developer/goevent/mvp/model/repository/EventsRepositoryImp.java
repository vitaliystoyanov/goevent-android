package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;

import java.util.List;

public class EventsRepositoryImp implements EventsRepository {
    private static final String TAG = "EventsRepositoryImp";
    private final EventsLocalStorage localDataSource;
    private final EventsBackendService remoteDataSource;
    private OnNotReceiveRemoteListener listener;

    public EventsRepositoryImp(EventsLocalStorage localDataSource,
                               EventsBackendService remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Nullable
    @Override
    public List<Event> getEvents() {
        List<Event> events = remoteDataSource.getEvents();
        if (events != null) {
            localDataSource.saveEvents(events);
        }
        if (events == null && listener != null) {
            listener.notReceive();
        }
        return localDataSource.getEvents();
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {

    }

    @Override
    public void addOnNotReceiveRemoteListener(OnNotReceiveRemoteListener listener) {
        this.listener = listener;
    }
}
