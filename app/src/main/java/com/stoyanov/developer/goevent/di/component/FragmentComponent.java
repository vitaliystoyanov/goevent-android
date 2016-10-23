package com.stoyanov.developer.goevent.di.component;

import com.stoyanov.developer.goevent.di.module.FragmentModule;
import com.stoyanov.developer.goevent.di.module.PresenterModule;
import com.stoyanov.developer.goevent.di.scope.FragmentScope;
import com.stoyanov.developer.goevent.ui.fragment.ListOfEventsFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = {PresenterModule.class,
        FragmentModule.class})
public interface FragmentComponent {

    void inject(ListOfEventsFragment fragment);
}
