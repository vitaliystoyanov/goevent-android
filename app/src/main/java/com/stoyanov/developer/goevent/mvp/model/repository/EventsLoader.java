package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import java.util.List;

public class EventsLoader extends AsyncTaskLoader<List<Events>> {

    public EventsLoader(Context context) {
        super(context);
    }

    @Override
    public List<Events> loadInBackground() {
        return null;
    }

}
