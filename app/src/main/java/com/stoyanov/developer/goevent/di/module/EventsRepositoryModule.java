package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.cache.Cache;
import com.stoyanov.developer.goevent.mvp.model.repository.cache.CacheInMemory;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;

import dagger.Module;
import dagger.Provides;

@Module
public class EventsRepositoryModule {

    @Provides
    Cache<Event> provideCache() {
        return new CacheInMemory<Long, Event>();
    }

    @Provides
    EventsLocalStorage provideEventsLocalDataSource() {
        return new EventsLocalStorageImp();
    }

    @Provides
    EventsBackendService provideEventsRemoteDataSource(Application application) {
        return new EventsBackendServiceImp(application);
    }

    @Provides
    EventsRepository provideEventsRepository(Cache<Event> cache, EventsLocalStorage local,
                                             EventsBackendService remote) {
        return new EventsRepositoryImp(cache, local, remote);
    }
}
