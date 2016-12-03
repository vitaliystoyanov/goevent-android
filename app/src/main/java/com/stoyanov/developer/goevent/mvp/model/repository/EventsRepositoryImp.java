package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;

import java.util.Iterator;
import java.util.List;

public class EventsRepositoryImp implements EventsRepository {
    private static final String TAG = "EventsRepositoryImp";
    private final EventsStorage localEventsStorage;
    private final EventsBackendService remoteBackendService;
    private OnNotReceiveRemoteListener listener;

    public EventsRepositoryImp(EventsStorage localDataSource,
                               EventsBackendService remoteBackendService) {
        this.localEventsStorage = localDataSource;
        this.remoteBackendService = remoteBackendService;
    }

    @Nullable
    @Override
    public List<Event> getEvents() {
        List<Event> events = remoteBackendService.getEvents();
        if (events != null) {
            localEventsStorage.saveEvents(events);
        }
        if (events == null && listener != null) {
            listener.notReceive();
        }
        return localEventsStorage.getEvents();
    }

    @Override
    public List<Event> getEventsEliminateNullLocation() {
        List<Event> events = remoteBackendService.getEvents();
        if (events != null) {
            localEventsStorage.saveEvents(events);
        }
        if (events == null && listener != null) {
            listener.notReceive();
        }
        return removeNullLocation(localEventsStorage.getEvents());
    }

    @NonNull
    private List<Event> removeNullLocation(List<Event> data) {
        Iterator<Event> iterator = data.iterator();
        while (iterator.hasNext()) {
            Event next = iterator.next();
            if (next.getLocation() == null) {
                iterator.remove();
            }
        }
        return data;
    }

    @Override
    public void addOnNetworkErrorListener(OnNotReceiveRemoteListener listener) {
        this.listener = listener;
    }
}
