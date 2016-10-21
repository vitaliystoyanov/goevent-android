package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;

import java.util.ArrayList;
import java.util.List;

public class EventsRepositoryImp implements EventsRepository {
    private static final String TAG = "EventsRepositoryImp";
    private final EventsLocalStorage localDataSource;
    private final EventsBackendService remoteDataSource;
    private List<Observer> observers = new ArrayList<>();

    public EventsRepositoryImp(EventsLocalStorage localDataSource,
                               EventsBackendService remoteDataSource) {
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

        return remoteDataSource.getEvents();
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {

    }

    interface Observer {

        void onEventsChanged();

    }
}
