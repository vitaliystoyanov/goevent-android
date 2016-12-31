package com.stoyanov.developer.goevent.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.LastDefinedLocation;
import com.stoyanov.developer.goevent.mvp.presenter.NearbyEventsPresenter;
import com.stoyanov.developer.goevent.mvp.view.NearbyEventsView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NearbyEventsFragment extends Fragment
        implements NearbyEventsView, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "NearbyEventsFragment";
    @Inject
    NearbyEventsPresenter presenter;
    @Inject
    LocationManager locationManager;
    @BindView(R.id.nearby_events_mapview)
    MapView mapView;
    @BindView(R.id.nearby_floating_search_view)
    FloatingSearchView floatingSearchView;
    boolean isConnectedGoogleApi;
    private GoogleMap map;
    private Unbinder unbinder;
    private ClusterManager<Event> clusterManager;
    private GoogleApiClient googleApiClient;
    private LastDefinedLocation lastUserLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby_events, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupDagger();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        floatingSearchView.attachNavigationDrawerToMenuButton(((MainActivity) getActivity()).getDrawerLayout());
        floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {

            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.floating_search_my_location) {
                    presenter.onActionMenuMyLocation();
                }
            }
        });
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void setupDagger() {
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
    }

    @Override
    public void showMarkers(List<Event> events) {
        Log.d(TAG, "showMarkers: events size = " + events.size());
        clusterManager.addItems(events);
        clusterManager.cluster();
        Snackbar.make(mapView, events.size() != 0 ?
                        events.size() + " events for you!"
                        : "On this location doesn't have events. Try anywhere",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessageNetworkError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),
                        R.string.message_bad_connection,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void myLocation() {
        if (isConnectedGoogleApi) {
            updateLastLocation();
        } else {
            Toast.makeText(getContext(), "Google Fused Location isn't connected. Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        clusterManager = new ClusterManager<>(getActivity(), map);
        map.setOnMarkerClickListener(clusterManager);
        map.setOnCameraChangeListener(clusterManager);

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_maps_style);
        map.setMapStyle(style);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        lastUserLocation = locationManager.getLastDefinedLocation();
        if (lastUserLocation != null) {
            LatLng cameraPosition = new LatLng(lastUserLocation.getLatitude(),
                    lastUserLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 13));
        }
        presenter.onMapReady(lastUserLocation);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
        presenter.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
        presenter.onStop();
        presenter.detach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            LatLng cameraPosition = new LatLng(location.getLatitude(),
                    location.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 14));

            presenter.onUpdateLastLocation(new LastDefinedLocation(location.getLatitude(),
                    location.getLongitude()));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnectedGoogleApi = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
        isConnectedGoogleApi = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
        isConnectedGoogleApi = false;
    }
}
