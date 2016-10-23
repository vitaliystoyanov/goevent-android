package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.FavoriteEvent;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoritesEventsStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;

import java.util.List;

public class EventsRepositoryImp implements EventsRepository {
    private static final String TAG = "EventsRepositoryImp";
    private final EventsStorage localEventsStorage;
    private final EventsBackendService remoteBackendService;
    private final FavoritesEventsStorage favoritesEventsStorage;
    private OnNotReceiveRemoteListener listener;

    public EventsRepositoryImp(EventsStorage localDataSource,
                               EventsBackendService remoteBackendService,
                               FavoritesEventsStorage favoritesEventsStorage) {
        this.localEventsStorage = localDataSource;
        this.remoteBackendService = remoteBackendService;
        this.favoritesEventsStorage = favoritesEventsStorage;
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
    public void saveEvents(@NonNull List<Event> events) {

    }

    @Override
    public void addOnNotReceiveRemoteListener(OnNotReceiveRemoteListener listener) {
        this.listener = listener;
    }

    @Override
    public void add(@NonNull FavoriteEvent event) {
        favoritesEventsStorage.add(event);
    }

    @Override
    public void remove(@NonNull FavoriteEvent event) {
        favoritesEventsStorage.remove(event);
    }

    @Nullable
    @Override
    public List<FavoriteEvent> get() {
        return favoritesEventsStorage.get();
    }
}
