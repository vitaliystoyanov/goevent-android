package com.stoyanov.developer.goevent.mvp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.data.Event;
import com.stoyanov.developer.goevent.mvp.data.source.cache.Cache;
import com.stoyanov.developer.goevent.mvp.data.source.local.EventsLocalDataSource;
import com.stoyanov.developer.goevent.mvp.data.source.remote.EventsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class EventsRepository implements EventsDataSource {

    private final EventsLocalDataSource localDataSource;
    private final EventsRemoteDataSource remoteDataSource;
    private List<Observer> observers = new ArrayList<>();
    @Inject Cache<Event> cacheEvents;

    @Inject
    public EventsRepository(@NonNull EventsLocalDataSource localDataSource,
                            @NonNull EventsRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

    }

    public void addContentObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeContentObserver(Observer observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (Observer observer : observers) {
            observer.onEventsChanged();
        }
    }

    @Nullable
    @Override
    public List<Event> getEvents() {
        List<Event> events = null;
        if (cacheEvents.isCacheValid()) {
            if (cacheEvents.cachedAvailable()) {
                events = cacheEvents.getCached();
            } else {
                events = localDataSource.getEvents();
            }
        }
        if (events == null || events.isEmpty()) {
            events = remoteDataSource.getEvents();
        }
        localDataSource.saveEvents(events);
        cacheEvents.cache(events);
        return cacheEvents.getCached();
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {
        // FIXME: 01.10.2016
    }

    interface Observer {

        void onEventsChanged();

    }
}
