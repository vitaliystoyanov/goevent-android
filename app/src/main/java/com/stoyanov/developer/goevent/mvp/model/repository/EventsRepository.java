package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stoyanov.developer.goevent.mvp.model.domain.Events;
import com.stoyanov.developer.goevent.mvp.model.repository.cache.Cache;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalDataSource;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class EventsRepository implements EventsDataSource {
    private static final String TAG = "EventsRepository";
    private final EventsLocalDataSource localDataSource;
    private final EventsRemoteDataSource remoteDataSource;
    private List<Observer> observers = new ArrayList<>();
    private Cache<Events> cacheEvents;

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
    public Events getEvents() {
/*        List<Events> events = null;
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
        return cacheEvents.getCached();*/
        return null;
    }

    @Override
    public void saveEvents(@NonNull List<Events> events) {
        // FIXME: 01.10.2016
    }

    @Nullable
    public Cache<Events> getCacheEvents() {
        Log.d(TAG, "EventsRepository: is cache null? -> " + (cacheEvents == null));
        return cacheEvents;
    }

    public void setCacheEvents(Cache<Events> cacheEvents) {
        this.cacheEvents = cacheEvents;
    }

    interface Observer {

        void onEventsChanged();

    }
}
