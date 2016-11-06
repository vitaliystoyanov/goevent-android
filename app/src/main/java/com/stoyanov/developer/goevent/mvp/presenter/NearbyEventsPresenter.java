package com.stoyanov.developer.goevent.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsLoader;
import com.stoyanov.developer.goevent.mvp.view.NearbyEventsView;

import java.util.List;

public class NearbyEventsPresenter extends BasePresenter<NearbyEventsView>
        implements LoaderManager.LoaderCallbacks<List<Event>> {
    private static final String TAG = "NearbyEventsPresenter";
    private static final int EVENTS_LOADER_ID = 2;
    private final LoaderManager loaderManager;
    private final Context context;

    public NearbyEventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        this.context = context;
    }

    public void onMapReady() {
        loaderManager.initLoader(EVENTS_LOADER_ID, null, this);
        getView().showMessageYourLastLocation();
    }

    public void onStop() {
        Log.d(TAG, "onStop: ");
        loaderManager.destroyLoader(EVENTS_LOADER_ID);
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new EventsLoader(context, EventsLoader.FILTER.ELIMINATE_NULL_LOCATION) {
            @Override
            public void onNetworkError() {
                if (getView() != null) getView().showMessageNetworkError();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        getView().showMarkers(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
