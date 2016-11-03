package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.mvp.presenter.DetailPresenter;
import com.stoyanov.developer.goevent.mvp.view.DetailEventView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;
import com.stoyanov.developer.goevent.utill.DateUtil;

import javax.inject.Inject;

public class DetailEventFragment extends Fragment
        implements DetailEventView, OnMapReadyCallback {
    public static final String EXTRA_PARCELABLE_EVENT = "EXTRA_PARCELABLE_EVENT";
    @Inject
    NavigationManager navigationManager;
    @Inject
    DetailPresenter presenter;
    private MapView mapView;
    private GoogleMap map;
    private CoordinatorLayout coordinatorLayout;
    private ExpandableTextView expandableTextView;
    private LatLng location;
    private TextView durationTime;
    private TextView countryAndCity;
    private TextView street;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        setupToolbar();
        coordinatorLayout = (CoordinatorLayout) getView().findViewById(R.id.detail_event_coordinator);
        expandableTextView = (ExpandableTextView) getView().findViewById(R.id.description_expand_text_view);
        durationTime = (TextView) getView().findViewById(R.id.event_info_duration_time);
        countryAndCity = (TextView) getView().findViewById(R.id.event_info_location_county_city);
        street = (TextView) getView().findViewById(R.id.event_info_location_street);
        getView().findViewById(R.id.detail_event_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLikeClick();
            }
        });
        mapView = (MapView) getView().findViewById(R.id.event_map_routes_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.detail_event_toolbar);
        toolbar.setTitle(R.string.title_detail_event);
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
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        presenter.onStart((Event) getArguments().getParcelable(EXTRA_PARCELABLE_EVENT));
    }

    @Override
    public void showImage(String url) {
        if (url != null) {
            Picasso.with(getContext())
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into((ImageView) getView().findViewById(R.id.detail_event_image));
        } else {
            // FIXME: 03.11.2016
        }
    }

    @Override
    public void showDescription(String desc, String name) {
        expandableTextView.setText(desc);
        ((TextView) getView().findViewById(R.id.description_event_name)).setText(name);
    }

    @Override
    public void showWhen(String dateFrom, String dateTo) {
        durationTime.setText(DateUtil.toDuration(DateUtil.toDate(dateFrom), DateUtil.toDate(dateTo)));
    }

    @Override
    public void showLocation(Location location) {
        countryAndCity.setText(getResources()
                .getString(R.string.location_country_city_format,
                        location.getCity(), location.getCountry())
        );
        if (location.getStreet() != null) {
            street.setText(location.getStreet());
        } else {
            street.setVisibility(View.GONE);
        }
        this.location = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void showRoutes() {

    }

    @Override
    public void showMessageAddedToFavorites() {
        Snackbar.make(coordinatorLayout, R.string.message_event_added_favorite, Snackbar.LENGTH_LONG)
                .setAction("Favorite", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigationManager.goToFavorites();
                    }
                })
                .setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorActionText))
                .show();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
        map.addMarker(new MarkerOptions().position(this.location));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
