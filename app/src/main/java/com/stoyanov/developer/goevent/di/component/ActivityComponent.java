package com.stoyanov.developer.goevent.di.component;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.di.module.EventsRepositoryModule;
import com.stoyanov.developer.goevent.di.scope.ActivityScope;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Application app();

    LoaderManager manager();

    void inject(MainActivity activity);

}
