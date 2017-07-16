package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.manager.FavoriteEventManager;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoriteEventLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoriteEventLocalStorageImp;
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
    FavoriteEventLocalStorage provideFavoritesEventsStorage() {
        return new FavoriteEventLocalStorageImp();
    }

    @Provides
    FavoriteEventManager provideFavoriteEventsManager(FavoriteEventLocalStorage storage) {
        return new FavoriteEventManager(storage);
    }

    @Provides
    EventsRepository provideEventsRepository(EventsLocalStorage local,
                                             EventsBackendService remote) {
        return new EventsRepositoryImp(local, remote);
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager() {
        return new LocationManager();
    }
}
