package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.cache.Cache;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;

import java.util.ArrayList;
import java.util.List;

public class EventsRepositoryImp implements EventsRepository {
    private static final String TAG = "EventsRepositoryImp";
    private final EventsLocalStorage localDataSource;
    private final EventsBackendService remoteDataSource;
    private List<Observer> observers = new ArrayList<>();
    private Cache<Event> cache;

    public EventsRepositoryImp(Cache<Event> cache, EventsLocalStorage localDataSource,
                               EventsBackendService remoteDataSource) {
        this.cache = cache;
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
/*        List<Event> events = null;
        if (cache.isCacheValid()) {
            events = cache.getCached();
            Log.d(TAG, "getEvents: Events got from cache");
        } else {
            events = localDataSource.getEvents();
            Log.d(TAG, "getEvents: Events got from DB");
        }
        if (events == null || events.isEmpty()) {
            events = remoteDataSource.getEvents();
            Log.d(TAG, "getEvents: Events got from server");
        }
        localDataSource.saveEvents(events);
        cache.cache(events);
        return cache.getCached();*/

/*        List<Event> events = remoteDataSource.getEvents();
        if (events != null) {
            Log.d(TAG, "getEvents: Events saved into DB");
            localDataSource.saveEvents(events);
        }*/
        return remoteDataSource.getEvents();
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {

    }

    @Nullable
    public Cache<Event> getCache() {
        Log.d(TAG, "EventsRepositoryImp: is cache null? -> " + (cache == null));
        return cache;
    }

    interface Observer {

        void onEventsChanged();

    }
}
