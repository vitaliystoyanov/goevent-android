package com.stoyanov.developer.goevent.di.module;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.mvp.presenter.DetailPresenter;
import com.stoyanov.developer.goevent.mvp.presenter.ListOfEventsPresenter;
import com.stoyanov.developer.goevent.mvp.presenter.NearbyEventsPresenter;
import com.stoyanov.developer.goevent.mvp.presenter.SavedEventsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    ListOfEventsPresenter providePresenter(Application application, LoaderManager manager) {
        return new ListOfEventsPresenter(application, manager);
    }

    @Provides
    DetailPresenter provideDetailPresenter() {
        return new DetailPresenter();
    }

    @Provides
    SavedEventsPresenter provideFavoriteEventsPresenter(Application application) {
        return new SavedEventsPresenter(application);
    }

    @Provides
    NearbyEventsPresenter provideNearbyEventsPresenter(Application application, LoaderManager manager) {
        return new NearbyEventsPresenter(application, manager);
    }
}
