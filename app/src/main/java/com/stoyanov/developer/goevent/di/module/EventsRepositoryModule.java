package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.cache.Cache;
import com.stoyanov.developer.goevent.mvp.model.repository.cache.CacheInMemory;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalDataSource;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsRemoteDataSource;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.UriBuilder;

import dagger.Module;
import dagger.Provides;

@Module
public class EventsRepositoryModule {

    @Provides
    Cache<Events> provideCache() {
        return new CacheInMemory<String, Events>();
    }

    @Provides
    EventsLocalDataSource provideEventsLocalDataSource(Application application) {
        return new EventsLocalDataSource();
    }

    @Provides
    UriBuilder provideUriBuilder(Application application) {
        return new UriBuilder(application.getString(R.string.host),
                application.getString(R.string.port));
    }

    @Provides
    EventsRemoteDataSource provideEventsRemoteDataSource(UriBuilder uriBuilder,
                                                         Application application) {
        return new EventsRemoteDataSource(uriBuilder, application);
    }

    @Provides
    EventsRepository provideEventsRepository(Cache<Events> cache, EventsLocalDataSource local,
                                             EventsRemoteDataSource remote) {
        EventsRepository repository = new EventsRepository(local, remote);
        repository.setCacheEvents(cache);
        return repository;
    }
}
