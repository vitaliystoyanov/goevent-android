package com.stoyanov.developer.goevent.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsLoader;
import com.stoyanov.developer.goevent.mvp.view.ListEventsView;

import java.util.List;

public class ListEventsPresenter extends BasePresenter<ListEventsView>
        implements LoaderManager.LoaderCallbacks<List<Event>> {
    private static final String TAG = "ListEventsPresenter";
    private final static int EVENTS_QUERY = 1;
    private LoaderManager loaderManager;
    private Loader<List<Event>> loader;

    public ListEventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        loader = new EventsLoader(context) {
            @Override
            public void onNotReceiveRemote() {
                if (getView() != null) getView().showMessageOnNotReceiveRemote();
            }
        };
    }

    public void onStart() {
        loaderManager.initLoader(EVENTS_QUERY, null, this);
        if (getView() != null) getView().showProgressBar(true);
    }

    public void onRefresh() {
        loader.forceLoad();
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        Log.d(TAG, "onLoadFinished: ");
        if (data != null && data.size() > 0) {
            getView().showEvents(data);
        } else {
            getView().showEmpty();
        }
        getView().showProgressBar(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
    }
}
