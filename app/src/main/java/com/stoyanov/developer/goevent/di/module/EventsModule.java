package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.RxProviders;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoriteLocalStorage;
import com.stoyanov.developer.goevent.mvp.model.repository.local.FavoriteLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsService;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsServiceImp;

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
    EventsService provideEventsRemoteDataSource(Application application) {
        return new EventsServiceImp(application);
    }

    @Provides
    FavoriteLocalStorage provideFavoritesEventsStorage() {
        return new FavoriteLocalStorageImp();
    }

    @Provides
    FavoriteManager provideFavoriteEventsManager(FavoriteLocalStorage storage) {
        return new FavoriteManager(storage);
    }

    @Provides
    EventsRepository provideEventsRepository(EventsLocalStorage local,
                                             EventsService remote,
                                             RxProviders rxProviders) {
        return new EventsRepositoryImp(local, remote, rxProviders);
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager() {
        return new LocationManager();
    }
}
