package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

import javax.inject.Inject;

public abstract class EventsLoader extends AsyncTaskLoader<List<Event>>
        implements EventsRepositoryImp.OnNotReceiveRemoteListener {
    private static final String TAG = "EventsLoader";

    @Inject
    EventsRepository repository;

    public EventsLoader(Context context) {
        super(context);
        (MainApplication.getApplicationComponent(context)).inject(this);
        repository.addOnNotReceiveRemoteListener(this);
    }

    @Override
    public List<Event> loadInBackground() {
        return repository.getEvents();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public abstract void onNotReceiveRemote();

    @Override
    public void notReceive() {
        onNotReceiveRemote();
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset: OnNotReceiveRemoteListener release");
        repository.addOnNotReceiveRemoteListener(null);
    }
}
