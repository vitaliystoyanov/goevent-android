package com.stoyanov.developer.goevent.di.component;

import android.app.Application;

import com.stoyanov.developer.goevent.di.module.ApplicationModule;
import com.stoyanov.developer.goevent.di.module.EventsModule;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsByLocationLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;
import com.stoyanov.developer.goevent.mvp.presenter.EventsPresenter;
import com.stoyanov.developer.goevent.mvp.presenter.SavedEventsPresenter;
import com.stoyanov.developer.goevent.ui.activity.DefaultLocationActivity;
import com.stoyanov.developer.goevent.ui.adapter.EventsAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, EventsModule.class})
public interface ApplicationComponent {

    Application application();

    LocationManager locationManager();

    void inject(EventsBackendServiceImp remoteDataSource);

    void inject(EventsLoader loader);

    void inject(SavedEventsLoader loader);

    void inject(EventsPresenter presenter);

    void inject(SavedEventsPresenter presenter);

    void inject(EventsAdapter adapter);

    void inject(EventsByLocationLoader loader);

    void inject(DefaultLocationActivity activity);

}
