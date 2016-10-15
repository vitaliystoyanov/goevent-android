package com.stoyanov.developer.goevent.di.component;

import android.app.Application;

import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.di.module.ApplicationModule;
import com.stoyanov.developer.goevent.di.module.EventsRepositoryModule;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, EventsRepositoryModule.class})
public interface ApplicationComponent {

    EventsRepository eventsRepository();

    Application application();

    void inject(EventsBackendServiceImp remoteDataSource);

}
