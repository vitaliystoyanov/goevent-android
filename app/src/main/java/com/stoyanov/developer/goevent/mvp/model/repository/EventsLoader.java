package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.filter.Comparators;
import com.stoyanov.developer.goevent.mvp.model.filter.FilterManager;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public abstract class EventsLoader extends AsyncTaskLoader<List<Event>>
        implements EventsRepositoryImp.OnNotReceiveRemoteListener {
    private static final String TAG = "EventsLoader";
    @Inject
    EventsRepository repository;
    private SORTING_PARAM sortingParam;
    private FILTER_PARAM filter;
    private Set<Category> categories;

    public EventsLoader(Context context, FILTER_PARAM filter) {
        super(context);
        this.filter = filter;
        sortingParam = SORTING_PARAM.NOT;
        (MainApplication.getApplicationComponent(context)).inject(this);
        repository.addOnNetworkErrorListener(this);
    }

    @Override
    public List<Event> loadInBackground() {
        List<Event> loaded = null;
        if (filter == FILTER_PARAM.ALL) {
            loaded = repository.getEvents();
        } else if (filter == FILTER_PARAM.ELIMINATE_NULL_LOCATION) {
            loaded = repository.getEventsEliminateNullLocation();
        }

        if (loaded == null) return null;

        if (sortingParam == SORTING_PARAM.DATE) {
            loaded = FilterManager.filterByDateAndAllCategory(loaded);
        } else if (sortingParam == SORTING_PARAM.LOCATION) {
            Collections.sort(loaded, new Comparators.EventsComparatorLocation());
        }
        return loaded;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: ");
        forceLoad();
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void setSortingParam(SORTING_PARAM param) {
        sortingParam = param;
    }

    public abstract void onNetworkError();

    @Override
    public void notReceive() {
        onNetworkError();
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset: OnNotReceiveRemoteListener is null");
        repository.addOnNetworkErrorListener(null);
    }

    public enum FILTER_PARAM {
        ELIMINATE_NULL_LOCATION, ALL
    }

    public enum SORTING_PARAM {
        DATE, LOCATION, NOT
    }
}
