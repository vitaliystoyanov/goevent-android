package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoritesEventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoritesEventsStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendService;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;

import dagger.Module;
import dagger.Provides;

@Module
public class EventsRepositoryModule {

    @Provides
    EventsStorage provideEventsLocalDataSource() {
        return new EventsLocalStorageImp();
    }

    @Provides
    EventsBackendService provideEventsRemoteDataSource(Application application) {
        return new EventsBackendServiceImp(application);
    }

    @Provides
    FavoritesEventsStorage provideFavoritesEventsStorage() {
        return new FavoritesEventsLocalStorageImp();
    }

    @Provides
    EventsRepository provideEventsRepository(EventsStorage local,
                                             EventsBackendService remote,
                                             FavoritesEventsStorage favorites) {
        return new EventsRepositoryImp(local, remote, favorites);
    }
}
