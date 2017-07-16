package com.stoyanov.developer.goevent.di.module;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.ui.eventdetail.EventDetailPresenter;
import com.stoyanov.developer.goevent.ui.events.EventsPresenter;
import com.stoyanov.developer.goevent.ui.favorite.FavoriteEventsPresenter;
import com.stoyanov.developer.goevent.ui.nearby.NearbyEventsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    EventsPresenter providePresenter(Application application, LoaderManager manager) {
        return new EventsPresenter(application, manager);
    }

    @Provides
    EventDetailPresenter provideDetailPresenter() {
        return new EventDetailPresenter();
    }

    @Provides
    FavoriteEventsPresenter provideFavoriteEventsPresenter(Application application) {
        return new FavoriteEventsPresenter(application);
    }

    @Provides
    NearbyEventsPresenter provideNearbyEventsPresenter(Application application, LoaderManager manager) {
        return new NearbyEventsPresenter(application, manager);
    }
}
