package com.stoyanov.developer.goevent.di.component;

import com.stoyanov.developer.goevent.di.module.PresenterModule;
import com.stoyanov.developer.goevent.di.scope.FragmentScope;
import com.stoyanov.developer.goevent.ui.eventdetail.DetailEventFragment;
import com.stoyanov.developer.goevent.ui.events.EventsFragment;
import com.stoyanov.developer.goevent.ui.main.MainFragment;
import com.stoyanov.developer.goevent.ui.nearby.NearbyEventsFragment;
import com.stoyanov.developer.goevent.ui.favorite.FavoriteFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = {PresenterModule.class})
public interface FragmentComponent {

    void inject(EventsFragment fragment);

    void inject(DetailEventFragment fragment);

    void inject(NearbyEventsFragment fragment);

    void inject(FavoriteFragment fragment);

    void inject(MainFragment fragment);

}
