package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;

import java.util.Iterator;
import java.util.List;

public class EventsRepositoryImp implements EventsRepository {
    private final EventsLocalStorage localEventsLocalStorage;
    private final EventsBackendService remoteBackendService;
    private OnNotReceiveRemoteListener listener;

    public EventsRepositoryImp(EventsLocalStorage localDataSource,
                               EventsBackendService remoteBackendService) {
        this.localEventsLocalStorage = localDataSource;
        this.remoteBackendService = remoteBackendService;
    }

    @Nullable
    @Override
    public List<Event> getEvents() {
        List<Event> freshEvents = remoteBackendService.getEvents();
        if (freshEvents != null) {
            persist(freshEvents);
            return freshEvents;
        } else if (listener != null) {
            listener.notReceive();
        }
        return localEventsLocalStorage.getEvents();
    }

    @Override
    public List<Event> getEventsEliminateNullLocations() {
        List<Event> freshOrPersistedEvents = getEvents();
        return freshOrPersistedEvents != null ? removeNullLocation(freshOrPersistedEvents) : null;
    }

    @Nullable
    public List<Event> getEventsByLocation(double latitude, double longitude) {
        List<Event> events = remoteBackendService.getEventsByLocation(latitude, longitude);
        return events != null ? removeNullLocation(events) : null;
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

    private void persist(List<Event> data) {
        localEventsLocalStorage.saveEvents(data);
    }

    @Override
    public void setOnNetworkErrorListener(OnNotReceiveRemoteListener listener) {
        this.listener = listener;
    }
}
