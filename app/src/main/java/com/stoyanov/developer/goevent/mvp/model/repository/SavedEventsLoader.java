package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.SavedEvent;

import java.util.List;

import javax.inject.Inject;

public class SavedEventsLoader extends AsyncTaskLoader<List<SavedEvent>> {

    @Inject
    SavedEventsManager manager;

    public SavedEventsLoader(Context context) {
        super(context);
        (MainApplication.getApplicationComponent(context)).inject(this);
    }

    @Override
    public List<SavedEvent> loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
