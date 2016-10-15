package com.stoyanov.developer.goevent.di.component;

import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.di.module.EventsRepositoryModule;
import com.stoyanov.developer.goevent.di.scope.ActivityScope;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

}
