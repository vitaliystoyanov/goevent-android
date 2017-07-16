package com.stoyanov.developer.goevent.ui.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.manager.FavoriteEventManager;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.loader.EventsByLocationLoader;
import com.stoyanov.developer.goevent.mvp.model.loader.EventsLoader;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

public class EventsPresenter extends BasePresenter<EventsView>
        implements LoaderManager.LoaderCallbacks<List<Event>> {
    public static final int PAGE_EVENTS_CUSTOM_FILTER = 2;
    public static final int PAGE_EVENTS_BY_LOCATION = 1;
    public static final int PAGE_EVENTS_BY_DATE = 0;
    public final static int ID_LOADER_EVENTS = 11;
    @Inject
    FavoriteEventManager favoriteEventManager;
    private Event savedEvent;
    private EventsLoader.SORTING_PARAM sortingParam;
    private LocationPref definedLocation;
    private final LoaderManager loaderManager;
    private final Context context;

    public EventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        this.context = context;
        (GoeventApplication.getApplicationComponent(context)).inject(this);
    }

    public void onStart(LocationPref location) {
        definedLocation = location;
        loaderManager.initLoader(ID_LOADER_EVENTS, null, this);
        getView().showProgress(true);
    }

    public void onRefresh() {
        loaderManager.restartLoader(ID_LOADER_EVENTS, null, this);
    }

    public void onDestroyView() {
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
            favoriteEventManager.add(savedEvent);
        }
    }

    public void onUnlike() {
        if (savedEvent != null) favoriteEventManager.remove(savedEvent);
    }

    @SuppressLint("StaticFieldLeak")
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
            getView().showEvents(data);
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
