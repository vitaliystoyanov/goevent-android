package com.stoyanov.developer.goevent.mvp.model.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.filter.FilterManager;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public abstract class EventsLoader extends AsyncTaskLoader<List<Event>>
        implements EventsRepository.OnNotReceiveRemoteListener {
    private static final String TAG = "EventsLoader";
    @Inject
    EventsRepository repository;
    private Set<Category> categories;
    private SORTING_PARAM sortingParam;
    private FILTER_PARAM filter;

    public EventsLoader(Context context, FILTER_PARAM filter) {
        super(context);
        this.filter = filter;
        sortingParam = SORTING_PARAM.NOT;
        (GoeventApplication.getApplicationComponent(context)).inject(this);
        repository.setOnNetworkErrorListener(this);
    }

    @Override
    public List<Event> loadInBackground() {
        List<Event> data = null;
        if (filter == FILTER_PARAM.ALL) {
            data = repository.getEvents();
        } else if (filter == FILTER_PARAM.ELIMINATE_NULL_LOCATION) {
            data = repository.getEventsEliminateNullLocations();
        }

//        if (sortingParam == SORTING_PARAM.DATE) {
        data = FilterManager.filterByDateAndAllCategory(data);
        /*} else if (sortingParam == SORTING_PARAM.LOCATION) {
            Collections.sort(data, new Comparators.EventsComparatorLocation());
        }*/

        for (Event event : data) {
            categories.add(new Category(event.getCategory()));
        }
        return data;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: ");
        forceLoad();
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
        repository.setOnNetworkErrorListener(null);
    }

    public enum FILTER_PARAM {
        ELIMINATE_NULL_LOCATION, ALL
    }

    public enum SORTING_PARAM {
        DATE, LOCATION, NOT
    }
}
