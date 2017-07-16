package com.stoyanov.developer.goevent.di.component;

import android.app.Application;

import com.stoyanov.developer.goevent.di.module.ApplicationModule;
import com.stoyanov.developer.goevent.di.module.EventsModule;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.loader.EventsByLocationLoader;
import com.stoyanov.developer.goevent.mvp.model.loader.EventsLoader;
import com.stoyanov.developer.goevent.mvp.model.loader.FavoriteEventsLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;
import com.stoyanov.developer.goevent.ui.events.EventsPresenter;
import com.stoyanov.developer.goevent.ui.favorite.FavoriteEventsPresenter;
import com.stoyanov.developer.goevent.ui.location.DefaultLocationActivity;
import com.stoyanov.developer.goevent.ui.events.EventsAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, EventsModule.class})
public interface ApplicationComponent {

    Application application();

    LocationManager locationManager();

    void inject(EventsBackendServiceImp remoteDataSource);

    void inject(EventsLoader loader);

    void inject(FavoriteEventsLoader loader);

    void inject(EventsPresenter presenter);

    void inject(FavoriteEventsPresenter presenter);

    void inject(EventsAdapter adapter);

    void inject(EventsByLocationLoader loader);

    void inject(DefaultLocationActivity activity);

}
