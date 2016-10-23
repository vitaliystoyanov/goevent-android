package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.view.MapEventsView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;

public class NearbyEventsFragment extends Fragment implements MapEventsView, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private FloatingSearchView floatingSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby_events, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        floatingSearchView = (FloatingSearchView) getView().findViewById(R.id.nearby_floating_search_view);
        floatingSearchView.attachNavigationDrawerToMenuButton(((MainActivity)getActivity()).getDrawerLayout());
        mapView = (MapView) getView().findViewById(R.id.nearby_events_mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng cameraPosition = new LatLng(50.4565951, 30.4870897);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 10));
/*        List<Event> events = SugarRecord.listAll(Event.class);

        for (Event event: events) {
            if (event.getLocation() != null) {
                map.addMarker(new MarkerOptions()
                        .title(event.getName())
                        .snippet("")
                        .position(new LatLng(event.getLocation().getLongitude(),
                                event.getLocation().getLatitude())));
            }
        }*/
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
