package com.stoyanov.developer.goevent.di.module;

import android.app.Application;

import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.ui.eventdetail.EventDetailPresenter;
import com.stoyanov.developer.goevent.ui.events.EventsPresenter;
import com.stoyanov.developer.goevent.ui.favorite.FavoriteEventsPresenter;
import com.stoyanov.developer.goevent.ui.main.MainPresenter;
import com.stoyanov.developer.goevent.ui.nearby.NearbyEventsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    MainPresenter provideMainPresenter(Application context, EventsRepository repository) {
        return new MainPresenter(context, repository);
    }

    @Provides
    EventsPresenter providePresenter(Application application, FavoriteManager manager,
                                     EventsRepository repository) {
        return new EventsPresenter(application, manager, repository);
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
    NearbyEventsPresenter provideNearbyEventsPresenter(Application application, EventsRepository repository) {
        return new NearbyEventsPresenter(application, repository);
    }
}
