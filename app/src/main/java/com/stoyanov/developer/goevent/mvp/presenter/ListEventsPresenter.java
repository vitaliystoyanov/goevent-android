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
    private final static String TAG = "ListEventsPresenter";
    private final static int EVENTS_QUERY = 1;
    private final LoaderManager loaderManager;
    private final Context context;

    public ListEventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        this.context = context;
    }

    public void onStart() {
        Log.d(TAG, "onStart: ");
        loaderManager.initLoader(EVENTS_QUERY, null, this);
        if (getView() != null) getView().showProgressBar(true);
    }

    public void onRefresh() {
        loaderManager.restartLoader(EVENTS_QUERY, null, this);
    }

    public void onActionSearch() {
        getView().goToSearchEvents();
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        loaderManager.destroyLoader(EVENTS_QUERY);
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new EventsLoader(context) {
            @Override
            public void onNotReceiveRemote() {
                if (getView() != null) getView().showMessageOnNotReceiveRemote();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        Log.d(TAG, "onLoadFinished: ");
        if (getView() == null) return;
        if (data != null && data.size() > 0) {
            getView().showEvents(data);
        } else {
            getView().showEmpty();
        }
        getView().showProgressBar(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
