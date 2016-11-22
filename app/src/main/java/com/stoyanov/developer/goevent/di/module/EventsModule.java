package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsManager;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.SavedEventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.SavedEventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;

import dagger.Module;
import dagger.Provides;

@Module
public class EventsModule {

    @Provides
    EventsStorage provideEventsLocalDataSource() {
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
    EventsRepository provideEventsRepository(EventsStorage local,
                                             EventsBackendService remote) {
        return new EventsRepositoryImp(local, remote);
    }
}
