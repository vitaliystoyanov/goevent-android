package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsManager;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.SavedEventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.SavedEventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EventsModule {

    @Provides
    EventsLocalStorage provideEventsLocalDataSource() {
        return new EventsLocalStorageImp();
    }

    @Provides
    EventsBackendService provideEventsRemoteDataSource(Application application) {
        return new EventsBackendServiceImp(application);
    }

    @Provides
    SavedEventsLocalStorage provideFavoritesEventsStorage() {
        return new SavedEventsLocalStorageImp();
    }

    @Provides
    SavedEventsManager provideFavoriteEventsManager(SavedEventsLocalStorage storage) {
        return new SavedEventsManager(storage);
    }

    @Provides
    EventsRepository provideEventsRepository(EventsLocalStorage local,
                                             EventsBackendService remote) {
        return new EventsRepositoryImp(local, remote);
    }
}
