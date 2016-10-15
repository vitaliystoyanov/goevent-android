package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

import javax.inject.Inject;

public class EventsLoader extends AsyncTaskLoader<List<Event>> {

    @Inject
    EventsRepository repository;

    public EventsLoader(Context context) {
        super(context);
        (MainApplication.getApplicationComponent(context)).inject(this);
    }

    @Override
    public List<Event> loadInBackground() {
        return repository.getEvents();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
