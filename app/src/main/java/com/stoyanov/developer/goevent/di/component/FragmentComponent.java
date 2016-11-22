package com.stoyanov.developer.goevent.di.component;

import com.stoyanov.developer.goevent.di.module.PresenterModule;
import com.stoyanov.developer.goevent.di.scope.FragmentScope;
import com.stoyanov.developer.goevent.ui.fragment.DetailEventFragment;
import com.stoyanov.developer.goevent.ui.fragment.SavedEventsFragment;
import com.stoyanov.developer.goevent.ui.fragment.ListOfEventsFragment;
import com.stoyanov.developer.goevent.ui.fragment.NearbyEventsFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = {PresenterModule.class})
public interface FragmentComponent {

    void inject(ListOfEventsFragment fragment);

    void inject(DetailEventFragment fragment);

    void inject(NearbyEventsFragment fragment);

    void inject(SavedEventsFragment fragment);

}
