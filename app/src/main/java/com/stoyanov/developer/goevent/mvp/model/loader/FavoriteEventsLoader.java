package com.stoyanov.developer.goevent.mvp.model.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.SavedEvent;
import com.stoyanov.developer.goevent.manager.FavoriteEventManager;

import java.util.List;

import javax.inject.Inject;

public class FavoriteEventsLoader extends AsyncTaskLoader<List<SavedEvent>> {

    @Inject
    FavoriteEventManager manager;

    public FavoriteEventsLoader(Context context) {
        super(context);
        (GoeventApplication.getApplicationComponent(context)).inject(this);
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
