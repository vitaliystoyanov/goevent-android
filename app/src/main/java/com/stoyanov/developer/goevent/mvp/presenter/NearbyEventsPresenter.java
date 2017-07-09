package com.stoyanov.developer.goevent.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsByLocationLoader;
import com.stoyanov.developer.goevent.mvp.view.NearbyEventsView;

import java.util.List;

public class NearbyEventsPresenter extends BasePresenter<NearbyEventsView>
        implements LoaderManager.LoaderCallbacks<List<Event>> {
    private static final String TAG = "NearbyEventsPresenter";
    private static final int EVENTS_BY_LOCATION_LOADER_ID = 4;
    private final LoaderManager loaderManager;
    private final Context context;
    private LocationPref lastLocationPref;

    public NearbyEventsPresenter(Context context, LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
        this.context = context;
    }

    public void onMapReady(LocationPref location) {
        Log.d(TAG, "onMapReady: ");
        lastLocationPref = location;
        loaderManager.initLoader(EVENTS_BY_LOCATION_LOADER_ID, null, this);
    }

    public void onStop() {
        Log.d(TAG, "onStop: ");
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: id - " + id);
        getView().visibleProgress(true);
        return new EventsByLocationLoader(context, lastLocationPref) {
            @Override
            public void onNetworkError() {
                if (getView() != null) getView().showMessageNetworkError();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        if (data != null) Log.d(TAG, "onLoadFinished: data size is " + data.size());
        getView().showMarkers(data);
        getView().visibleProgress(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        Log.d(TAG, "onLoaderReset: Loader was reset");
    }

    public void onActionMenuMyLocation() {
        getView().myLocation();
    }

    public void onUpdateSearchLocation(LocationPref location) {
        lastLocationPref = location;
        loaderManager.restartLoader(EVENTS_BY_LOCATION_LOADER_ID, null, this);
        getView().updateMapCamera(lastLocationPref, true);
    }

    public void onPageSelected(Event event) {
        updateMapCamera(event);
    }

    public void onClusterItemClick(Event event) {
        updateMapCamera(event);
    }

    private void updateMapCamera(Event event) {
        Location latLng = event.getLocation();
        if (latLng != null) {
            LocationPref location = new LocationPref(latLng.getLatitude(),
                    latLng.getLongitude());
            getView().updateMapCamera(location, false);
        }
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy: Destroying loader");
        loaderManager.destroyLoader(EVENTS_BY_LOCATION_LOADER_ID);
    }
}
