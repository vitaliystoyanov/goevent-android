package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.DefinedLocation;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

import javax.inject.Inject;

public abstract class EventsByLocationLoader extends AsyncTaskLoader<List<Event>>
        implements EventsRepositoryImp.OnNotReceiveRemoteListener {
    private static final String TAG = "EventsByLocationLoader";
    @Inject
    EventsRepository repository;
    private DefinedLocation definedLocation;

    public EventsByLocationLoader(Context context, @NonNull DefinedLocation location) {
        super(context);
        definedLocation = location;
        (MainApplication.getApplicationComponent(context)).inject(this);
        repository.setOnNetworkErrorListener(this);
    }

    @Override
    public List<Event> loadInBackground() {
        if (definedLocation == null) return null;
        return repository.getEventsByLocation(definedLocation.getLatitude(),
                definedLocation.getLongitude());
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
        Log.d(TAG, "onReset: OnNotReceiveRemoteListener is null");
        repository.setOnNetworkErrorListener(null);
    }
}
