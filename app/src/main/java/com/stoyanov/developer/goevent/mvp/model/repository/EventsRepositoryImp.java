package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
        getAndSaveEvents();
        return localEventsStorage.getEvents();
    }

    @Nullable
    @Override
    public List<Event> getEvents(@NonNull Set<Category> categories) {
        getAndSaveEvents();
        return localEventsStorage.getEvents(categories);
    }

    @Override
    public List<Event> getEventsEliminateNullLocation() {
        getAndSaveEvents();
        return removeNullLocation(localEventsStorage.getEvents());
    }

    public List<Event> getEventsByLocation(double latitude, double longitude) {
        List<Event> events = remoteBackendService.getEventsByLocation(latitude, longitude);
        return removeNullLocation(events);
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

    private void getAndSaveEvents() {
        List<Event> events = remoteBackendService.getEvents();
        if (events != null) {
            localEventsStorage.saveEvents(events);
        }
        if (events == null && listener != null) {
            listener.notReceive();
        }
    }

    @Override
    public void addOnNetworkErrorListener(OnNotReceiveRemoteListener listener) {
        this.listener = listener;
    }
}
