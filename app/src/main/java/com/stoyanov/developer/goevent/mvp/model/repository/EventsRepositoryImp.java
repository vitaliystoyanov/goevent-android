package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.rx_cache2.EvictProvider;

public class EventsRepositoryImp implements EventsRepository {
    private static final String KEY_EVENTS = "events";
    private static final int MAX_DISTANCE = 5000;
    private final EventsLocalStorage localStorage;
    private final EventsService remoteService;
    private OnNotReceiveRemoteListener listener;
    private RxProviders cacheProviders;

    public EventsRepositoryImp(EventsLocalStorage local,
                               EventsService remote, RxProviders rxProviders) {
        localStorage = local;
        remoteService = remote;
        cacheProviders = rxProviders;
    }

    @Override
    public List<Event> getEvents() {
        return null;
    }

    @Nullable
    public Single<List<Event>> getEventsBy(double latitude, double longitude, boolean updateCache) {
        Single<List<Event>> single = remoteService.getEventsByLocation(latitude, longitude).toObservable()
                .flatMap(events -> Observable.fromIterable(events.list()))
                .toList();
        return cacheProviders.getEventsEvictProvider(single, new EvictProvider(updateCache));
    }

    @Override
    public Single<List<Event>> getEventsBy(double latitude, double longitude, String since, String until, boolean updateCache) {
        return remoteService.getEventsByLocation(latitude, longitude, MAX_DISTANCE, since, until)
                .toObservable()
                .flatMap(events -> Observable.fromIterable(events.list()))
                .toList();
    }

    @Override
    public Single<List<Event>> getEventsBy(double latitude, double longitude,
                                           int distance, boolean updateCache) {
        Single<List<Event>> single = remoteService.getEventsByLocation(latitude, longitude, distance)
                .toObservable()
                .flatMap(events -> Observable.fromIterable(events.list()))
                .toList();
        return cacheProviders.getEventsEvictProvider(single, new EvictProvider(updateCache));
    }

    @Override
    public void setOnNetworkErrorListener(OnNotReceiveRemoteListener listener) {
        this.listener = listener;
    }
}
