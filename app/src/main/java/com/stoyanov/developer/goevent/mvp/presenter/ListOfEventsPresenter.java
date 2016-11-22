package com.stoyanov.developer.goevent.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsManager;
import com.stoyanov.developer.goevent.mvp.view.ListOfEventsView;

import java.util.List;

import javax.inject.Inject;

public class ListOfEventsPresenter extends BasePresenter<ListOfEventsView>
        implements LoaderManager.LoaderCallbacks<List<Event>> {
    private final static String TAG = "ListOfEventsPresenter";
    private final static int EVENTS_QUERY = 11;
    private final LoaderManager loaderManager;
    private final Context context;
    @Inject
    SavedEventsManager savedEventsManager;
    private Event savedEvent;

    public ListOfEventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        this.context = context;
        (MainApplication.getApplicationComponent(context)).inject(this);
    }

    public void onStart() {
        Log.d(TAG, "onStart: ");
        loaderManager.initLoader(EVENTS_QUERY, null, this);
        if (getView() != null) getView().showProgressBar(true);
    }

    public void onRefresh() {
        loaderManager.restartLoader(EVENTS_QUERY, null, this);
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        loaderManager.destroyLoader(EVENTS_QUERY);
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new EventsLoader(context, EventsLoader.FILTER.ALL) {
            @Override
            public void onNetworkError() {
                if (getView() != null) getView().showMessageNetworkError();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        if (getView() == null) return;
        if (data != null && data.size() > 0) {
            getView().showEvents(data);
        } else {
            getView().showEmpty();
        }
        getView().showProgressBar(false);
        if (data != null) Log.d(TAG, "onLoadFinished: data size:" + data.size());
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

    public void onItem(Event event) {
        getView().goToDetailEvent(event);
    }

    public void onActionSearch() {
        getView().goToSearchEvents();
    }

    public void onItemStar(Event item) {
        savedEvent = item;
    }

    public void onLike() {
        getView().showMessageAddedToFavorite();
        if (savedEvent != null) {
            Log.d(TAG, "onLike: savedEventsManager.add(savedEvent);");
            savedEventsManager.add(savedEvent);
        }
    }

    public void onUnlike() {
        if (savedEvent != null) savedEventsManager.remove(savedEvent);
    }
}
