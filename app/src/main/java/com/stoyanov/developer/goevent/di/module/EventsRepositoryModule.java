package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.mvp.data.Event;
import com.stoyanov.developer.goevent.mvp.data.source.EventsRepository;
import com.stoyanov.developer.goevent.mvp.data.source.cache.Cache;
import com.stoyanov.developer.goevent.mvp.data.source.cache.CacheInMemory;
import com.stoyanov.developer.goevent.mvp.data.source.local.EventsLocalDataSource;
import com.stoyanov.developer.goevent.mvp.data.source.remote.EventsRemoteDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class EventsRepositoryModule {

    @Provides
    Cache<Event> provideCache() {
        return new CacheInMemory<String, Event>();
    }

    @Provides
    EventsLocalDataSource provideEventsLocalDataSource(Application application) {
        return new EventsLocalDataSource();
    }

    @Provides
    EventsRemoteDataSource provideEventsRemoteDataSource(Application application) {
        return new EventsRemoteDataSource();
    }

    @Provides
    EventsRepository provideEventsRepository(Cache<Event> cache, EventsLocalDataSource local,
                                             EventsRemoteDataSource remote) {
        EventsRepository repository = new EventsRepository(local, remote);
        repository.setCacheEvents(cache);
        return repository;
    }
}
