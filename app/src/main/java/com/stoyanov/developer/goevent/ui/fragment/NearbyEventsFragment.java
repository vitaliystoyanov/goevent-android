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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
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
    @BindView(R.id.nearby_events_mapview)
    MapView mapView;
    @BindView(R.id.nearby_floating_search_view)
    FloatingSearchView floatingSearchView;
    private GoogleMap map;
    private Unbinder unbinder;
    private ClusterManager<Event> clusterManager;
    private GoogleApiClient mGoogleApiClient;

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
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        floatingSearchView.attachNavigationDrawerToMenuButton(((MainActivity) getActivity()).getDrawerLayout());
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
        floatingSearchView.hideProgress();
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
    public void showMessageYourLastLocation() {
        Snackbar.make(getView(), "Your last location is Kyiv, Ukraine", Snackbar.LENGTH_LONG)
                .setAction(R.string.message_action_update, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorActionText))
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        floatingSearchView.showProgress();
        clusterManager = new ClusterManager<>(getActivity(), map);
        map.setOnMarkerClickListener(clusterManager);
        map.setOnCameraChangeListener(clusterManager);

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_maps_style);
        map.setMapStyle(style);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        LatLng cameraPosition = new LatLng(50.4565951, 30.4870897);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 10));

        presenter.onMapReady();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        presenter.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        presenter.onStop();
        presenter.detach();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {
            Snackbar.make(getView(), "Your location is Lat: " + location.getLatitude() +
                    ", Lng: " + location.getLongitude(), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
