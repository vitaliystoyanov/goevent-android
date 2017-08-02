package com.stoyanov.developer.goevent.di.component;

import android.app.Application;

import com.stoyanov.developer.goevent.di.module.ApplicationModule;
import com.stoyanov.developer.goevent.di.module.EventsModule;
import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.RxProviders;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsServiceImp;
import com.stoyanov.developer.goevent.ui.events.EventsAdapter;
import com.stoyanov.developer.goevent.ui.events.EventsPresenter;
import com.stoyanov.developer.goevent.ui.favorite.FavoriteEventsPresenter;
import com.stoyanov.developer.goevent.ui.location.DefaultLocationActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, EventsModule.class})
public interface ApplicationComponent {

    Application application();

    LocationManager locationManager();

    FavoriteManager favoriteManager();

    RxProviders rxProviders();

    EventsRepository eventsRepository();

    void inject(EventsServiceImp remoteDataSource);

    void inject(EventsPresenter presenter);

    void inject(FavoriteEventsPresenter presenter);

    void inject(EventsAdapter adapter);

    void inject(DefaultLocationActivity activity);

}
