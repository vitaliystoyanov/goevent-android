package com.stoyanov.developer.goevent.di.module;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.di.scope.FragmentScope;
import com.stoyanov.developer.goevent.mvp.presenter.ListEventsPresenter;
import com.stoyanov.developer.goevent.mvp.view.ListEventsView;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    private ListEventsView listEventsView;

    public PresenterModule(ListEventsView listEventsView) {
        this.listEventsView = listEventsView;
    }

    @Provides
    @FragmentScope
    ListEventsPresenter providePresenter(Application application, LoaderManager manager) {
        return new ListEventsPresenter(application, manager, listEventsView);
    }
}
