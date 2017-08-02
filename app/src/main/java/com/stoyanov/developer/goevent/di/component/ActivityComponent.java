package com.stoyanov.developer.goevent.di.component;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.di.scope.ActivityScope;
import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.RxProviders;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.ui.location.DefaultLocationActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Application app();

    LoaderManager loaderManager();

    NavigationManager navigationManager();

    FavoriteManager favoriteEventManager();

    RxProviders rxProviders();

    EventsRepository eventsRepository();

    LocationManager locationManager();

    void inject(ContainerActivity activity);

    void inject(DefaultLocationActivity activity);

}
