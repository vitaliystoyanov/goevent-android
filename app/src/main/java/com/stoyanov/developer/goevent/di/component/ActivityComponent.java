package com.stoyanov.developer.goevent.di.component;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.di.scope.ActivityScope;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.ui.activity.DefaultLocationActivity;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Application app();

    LoaderManager loaderManager();

    NavigationManager navigationManager();

    LocationManager locationManager();

    void inject(MainActivity activity);

    void inject(DefaultLocationActivity activity);

}
