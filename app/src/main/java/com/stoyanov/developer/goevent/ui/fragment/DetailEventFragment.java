package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.DetailEventView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;

import javax.inject.Inject;

public class DetailEventFragment extends Fragment
        implements DetailEventView, OnMapReadyCallback {
    public static final String EXTRA_PARCELABLE_EVENT = "EXTRA_PARCELABLE_EVENT";
    @Inject NavigationManager navigationManager;
    private MapView mapView;
    private GoogleMap map;

    public static Fragment newInstance(Event event) {
        Bundle bundle = new Bundle();
        Fragment fragment = new DetailEventFragment();
        bundle.putParcelable(EXTRA_PARCELABLE_EVENT, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_event, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);

        Event event = (Event) getArguments().getParcelable(EXTRA_PARCELABLE_EVENT);
        if (event.getPicture() != null) {
            Picasso.with(getContext())
                    .load(event.getPicture())
                    .fit()
                    .centerCrop()
                    .into((ImageView) getView().findViewById(R.id.detail_event_image));
        }
//        Toast.makeText(getContext(), "Event: " + event.getName(), Toast.LENGTH_SHORT).show();
        setupToolbar();
        ExpandableTextView expandableTextView =
                (ExpandableTextView) getView().findViewById(R.id.description_expand_text_view);
        expandableTextView.setText(event.getDescription());
        ((TextView) getView().findViewById(R.id.description_event_name)).setText(event.getName());


        mapView = (MapView) getView().findViewById(R.id.event_map_routes_map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.detail_event_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationManager.back(DetailEventFragment.this);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng cameraPosition = new LatLng(50.4565951, 30.4870897);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 12));
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
