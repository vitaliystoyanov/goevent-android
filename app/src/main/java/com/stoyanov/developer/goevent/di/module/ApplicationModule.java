package com.stoyanov.developer.goevent.di.module;

import android.content.Context;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.data.source.EventsRepository;
import com.stoyanov.developer.goevent.mvp.data.source.local.EventsLocalDataSource;
import com.stoyanov.developer.goevent.mvp.data.source.remote.EventsRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final MainApplication application;

    public ApplicationModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    EventsRepository provideEventsRepository(EventsLocalDataSource local,
                                             EventsRemoteDataSource remote) {
        return new EventsRepository(local, remote);
    }
}
