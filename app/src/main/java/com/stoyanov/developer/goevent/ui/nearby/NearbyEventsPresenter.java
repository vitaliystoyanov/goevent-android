package com.stoyanov.developer.goevent.ui.nearby;

import android.content.Context;
import android.support.annotation.NonNull;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsServiceImp;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NearbyEventsPresenter extends BasePresenter<NearbyEventsView> {
    private LocationPref lastLocationPref;
    private EventsRepository repository;
    private CompositeDisposable disposable;

    public NearbyEventsPresenter(Context context, EventsRepository repository) {
        disposable = new CompositeDisposable();
        this.repository = repository;
    }

    public void onMapReady(LocationPref location) {
        lastLocationPref = location;
        disposable.add(getEvents(location));
    }

    private Disposable getEvents(LocationPref location) {
        return repository.getEventsByLocation(location.getLatitude(), location.getLongitude(), true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(e -> getView().visibleProgress(true))
                .subscribe(events -> {
                    if (events != null && events.size() > 0) {
                        getView().showMarkers(removeNullLocation(events));
                    }
                    getView().visibleProgress(false);
                });
    }

    @NonNull
    private List<Event> removeNullLocation(List<Event> data) {
        Iterator<Event> iterator = data.iterator();
        while (iterator.hasNext()) {
            Event next = iterator.next();
            if (next.getLocation() == null) {
                iterator.remove();
            }
        }
        return data;
    }

    public void onActionMenuMyLocation() {
        getView().myLocation();
    }

    public void onUpdateSearchLocation(LocationPref location) {
        lastLocationPref = location;
        disposable.add(getEvents(location));
        getView().updateMapCamera(lastLocationPref, true);
    }

    public void onPageSelected(Event event) {
        updateMapCamera(event);
    }

    public void onClusterItemClick(Event event) {
        updateMapCamera(event);
    }

    private void updateMapCamera(Event event) {
        Location loc = event.getLocation();
        if (loc != null) {
            LocationPref pref = new LocationPref(loc.getLatitude(),
                    loc.getLongitude());
            getView().updateMapCamera(pref, false);
        }
    }

    public void pause() {
        disposable.clear();
    }
}
