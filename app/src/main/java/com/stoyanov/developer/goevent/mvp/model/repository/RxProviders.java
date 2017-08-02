package com.stoyanov.developer.goevent.mvp.model.repository;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderKey;

public interface RxProviders {

    @ProviderKey("events-provider")
    Single<List<Event>> getEventsEvictProvider(Single<List<Event>> oMocks, EvictProvider evictProvider);

}
