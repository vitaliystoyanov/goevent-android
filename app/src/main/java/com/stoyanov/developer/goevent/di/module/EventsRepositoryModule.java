package com.stoyanov.developer.goevent.di.module;

import com.stoyanov.developer.goevent.mvp.data.Event;
import com.stoyanov.developer.goevent.mvp.data.source.cache.Cache;
import com.stoyanov.developer.goevent.mvp.data.source.cache.CacheInMemory;

import dagger.Module;
import dagger.Provides;

@Module
public class EventsRepositoryModule {

    @Provides
    Cache<Event> provideCache() {
        return new CacheInMemory<String, Event>();
    }
}
