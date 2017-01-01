package com.stoyanov.developer.goevent.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.DefinedLocation;
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
    private DefinedLocation definedLocation;

    public ListOfEventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        this.context = context;
        (MainApplication.getApplicationComponent(context)).inject(this);
    }

    public void onStart(DefinedLocation location) {
        Log.d(TAG, "onStart: ");
        definedLocation = location;
        loaderManager.initLoader(ID_LOADER_EVENTS, null, this);
        getView().showProgress(true);
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        loaderManager.restartLoader(ID_LOADER_EVENTS, null, this);
    }

    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        loaderManager.destroyLoader(ID_LOADER_EVENTS);
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        EventsByLocationLoader loader = new EventsByLocationLoader(context, definedLocation) {
            @Override
            public void onNetworkError() {
                if (getView() != null) getView().showMessageNetworkError();
            }
        };
//        loader.setSortingParam(sortingParam != null ? sortingParam : EventsLoader.SORTING_PARAM.DATE);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        if (data != null && data.size() > 0) {
            getView().showEvents(data);
        } else {
            getView().showEmpty();
        }
        getView().showProgress(false);
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
//        getView().showMessageAddedToFavorite();
        if (savedEvent != null) {
            Log.d(TAG, "onLike: savedEventsManager.add(savedEvent);");
            savedEventsManager.add(savedEvent);
        }
    }

    public void onUnlike() {
        if (savedEvent != null) savedEventsManager.remove(savedEvent);
    }

    public void onClickViewPager(int page) {
        getView().showProgress(true);
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
