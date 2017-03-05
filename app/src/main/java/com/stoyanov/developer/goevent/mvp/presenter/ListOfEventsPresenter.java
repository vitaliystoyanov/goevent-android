package com.stoyanov.developer.goevent.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsByLocationLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsLoader;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsManager;
import com.stoyanov.developer.goevent.mvp.view.ListOfEventsView;

import java.util.List;

import javax.inject.Inject;

public class ListOfEventsPresenter extends BasePresenter<ListOfEventsView>
        implements LoaderManager.LoaderCallbacks<List<Event>> {
    public static final int PAGE_EVENTS_CUSTOM_FILTER = 2;
    public static final int PAGE_EVENTS_BY_LOCATION = 1;
    public static final int PAGE_EVENTS_BY_DATE = 0;
    public final static int ID_LOADER_EVENTS = 11;
    private final static String TAG = "ListOfEventsPresenter";
    private final LoaderManager loaderManager;
    private final Context context;
    @Inject
    SavedEventsManager savedEventsManager;
    private Event savedEvent;
    private EventsLoader.SORTING_PARAM sortingParam;
    private LocationPref locationPref;

    public ListOfEventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        this.context = context;
        (MainApplication.getApplicationComponent(context)).inject(this);
    }

    public void onStart(LocationPref location) {
        if (location != null) Log.d(TAG, "onStart: location arg is " + location.toString());
        locationPref = location;
        loaderManager.initLoader(ID_LOADER_EVENTS, null, this);
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        loaderManager.restartLoader(ID_LOADER_EVENTS, null, this);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        loaderManager.destroyLoader(ID_LOADER_EVENTS);
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        getView().visibleProgress(true);
        EventsByLocationLoader loader = new EventsByLocationLoader(context, locationPref) {
            @Override
            public void onNetworkError() {
                if (getView() != null) getView().showError();
            }
        };
//        loader.setSortingParam(sortingParam != null ? sortingParam : EventsLoader.SORTING_PARAM.DATE);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        Log.d(TAG, "onLoadFinished: Is data null?" + (data == null));
        if (data != null) {
            if (data.size() > 0) {
                getView().showEvents(data);
                Log.d(TAG, "onLoadFinished: data size is :" + data.size());
            } else {
                getView().showEmpty();
            }
        } else {
            getView().showError();
        }
        getView().visibleProgress(false);
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
//        getView().showMessageAdded();
        if (savedEvent != null) {
            Log.d(TAG, "onLike: savedEventsManager.add(savedEvent);");
            savedEventsManager.add(savedEvent);
        }
    }

    public void onUnlike() {
        if (savedEvent != null) savedEventsManager.remove(savedEvent);
    }

    public void onClickViewPager(int page) {
        getView().visibleProgress(true);
        getView().clearEvents();
        if (page == PAGE_EVENTS_BY_DATE) {
            sortingParam = EventsLoader.SORTING_PARAM.DATE;
        } else if (page == PAGE_EVENTS_BY_LOCATION) {
            sortingParam = EventsLoader.SORTING_PARAM.LOCATION;
        } else if (page == PAGE_EVENTS_CUSTOM_FILTER) {
            sortingParam = EventsLoader.SORTING_PARAM.NOT;
        }
        loaderManager.restartLoader(ID_LOADER_EVENTS, null, this);
    }
}
