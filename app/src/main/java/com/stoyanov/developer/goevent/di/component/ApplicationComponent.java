package com.stoyanov.developer.goevent.di.component;

import android.app.Application;

import com.stoyanov.developer.goevent.di.module.ApplicationModule;
import com.stoyanov.developer.goevent.di.module.EventsModule;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;
import com.stoyanov.developer.goevent.mvp.presenter.ListOfEventsPresenter;
import com.stoyanov.developer.goevent.mvp.presenter.SavedEventsPresenter;
import com.stoyanov.developer.goevent.ui.adapter.EventsAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, EventsModule.class})
public interface ApplicationComponent {

    Application application();

    void inject(EventsBackendServiceImp remoteDataSource);

    void inject(EventsLoader loader);

    void inject(SavedEventsLoader loader);

    void inject(ListOfEventsPresenter presenter);

    void inject(SavedEventsPresenter presenter);

    void inject(EventsAdapter adapter);

}
