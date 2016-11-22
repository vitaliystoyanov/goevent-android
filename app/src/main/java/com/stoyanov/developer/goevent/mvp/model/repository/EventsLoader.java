package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public abstract class EventsLoader extends AsyncTaskLoader<List<Event>>
        implements EventsRepositoryImp.OnNotReceiveRemoteListener {
    private static final String TAG = "EventsLoader";

    @Inject
    EventsRepository repository;
    private FILTER filter;

    public EventsLoader(Context context, FILTER filter) {
        super(context);
        this.filter = filter;
        (MainApplication.getApplicationComponent(context)).inject(this);
        repository.addOnNotReceiveRemoteListener(this);
    }

    @Override
    public List<Event> loadInBackground() {
        if (filter == FILTER.ALL) {
            Log.d(TAG, "loadInBackground: ALL");
            return repository.getEvents();
        } else if (filter == FILTER.ELIMINATE_NULL_LOCATION) {
            Log.d(TAG, "loadInBackground: ELIMINATE_NULL_LOCATION");
            return repository.getEventsEliminateNullLocation();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: ");
        forceLoad();
    }

    public abstract void onNetworkError();

    @Override
    public void notReceive() {
        onNetworkError();
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset: OnNotReceiveRemoteListener release");
        repository.addOnNotReceiveRemoteListener(null);
    }

    public enum FILTER {
        ELIMINATE_NULL_LOCATION, ALL
    }
}
