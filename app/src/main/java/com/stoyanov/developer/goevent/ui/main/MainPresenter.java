package com.stoyanov.developer.goevent.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.loader.EventsByLocationLoader;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

import java.util.List;

public class MainPresenter extends BasePresenter<MainView>
        implements LoaderManager.LoaderCallbacks<List<Event>> {
    private final static int ID_LOADER_EVENTS = 14;

    private LocationPref definedLocation;
    private LoaderManager loaderManager;
    private Context context;

    public MainPresenter(LoaderManager loaderManager, Context context) {
        this.loaderManager = loaderManager;
        this.context = context;
    }

    public void provideData(LocationPref location) {
        definedLocation = location;
        loaderManager.initLoader(ID_LOADER_EVENTS, null, this);
        getView().showProgress(true);
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        return new EventsByLocationLoader(context, definedLocation) {
            @Override
            public void onNetworkError() {
                if (getView() != null) getView().showMessageNetworkError();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        if (data != null && data.size() > 0) {
            getView().showPopularEvents(data);
            getView().showCategories(((EventsByLocationLoader) loader).getCategories());
        } else {
            getView().showEmpty();
        }
        getView().showProgress(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
    }
}
