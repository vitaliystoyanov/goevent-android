package com.stoyanov.developer.goevent.mvp.model.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public abstract class EventsByLocationLoader extends AsyncTaskLoader<List<Event>>
        implements EventsRepository.OnNotReceiveRemoteListener {
    private static final String TAG = "EventsByLocationLoader";
    @Inject
    EventsRepository repository;
    private LocationPref definedLocation;
    private Set<Category> categories;

    public EventsByLocationLoader(Context context, @NonNull LocationPref location) {
        super(context);
        definedLocation = location;
        (GoeventApplication.getApplicationComponent(context)).inject(this);
        repository.setOnNetworkErrorListener(this);
    }

    @Override
    public List<Event> loadInBackground() {
        if (definedLocation == null) return null;
        List<Event> data = repository.getEventsByLocation(definedLocation.getLatitude(),
                definedLocation.getLongitude());
        if (data != null) {
            categories = new HashSet<>();
            for (Event event : data) {
                categories.add(new Category(event.getCategory()));
            }
        }
        return data;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public abstract void onNetworkError();

    @Override
    public void notReceive() {
        onNetworkError();
    }

    @Override
    protected void onReset() {
        repository.setOnNetworkErrorListener(null);
    }
}
