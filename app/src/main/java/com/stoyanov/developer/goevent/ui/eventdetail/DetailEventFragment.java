package com.stoyanov.developer.goevent.ui.eventdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.utill.DateUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailEventFragment extends Fragment
        implements DetailEventView, OnMapReadyCallback {
    public static final String EXTRA_PARCELABLE_EVENT = "EXTRA_PARCELABLE_EVENT";
    public static final int ZOOM_LEVEL = 16;
    @Inject
    NavigationManager navigationManager;
    @Inject
    EventDetailPresenter presenter;

    @BindView(R.id.detail_event_routes_map)
    MapView mapView;
    @BindView(R.id.detail_event_coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.description_expand_text_view)
    ExpandableTextView expandableTextView;
    @BindView(R.id.detail_event_info_duration_time)
    TextView durationTime;
    @BindView(R.id.detail_event_info_location_county_city)
    TextView countryAndCity;
    @BindView(R.id.detail_event_info_location_street)
    TextView street;
    @BindView(R.id.detail_event_image)
    ImageView image;
    @BindView(R.id.detail_event_name)
    TextView eventName;
    @BindView(R.id.event_info_category)
    TextView category;
    @BindView(R.id.detail_event_toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_event_card_map)
    CardView cardViewMap;

    private GoogleMap map;
    private LatLng location;
    private Unbinder unbinder;
    private IconGenerator generator;

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
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_detail_event, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((ContainerActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        setupToolbar();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        generator = new IconGenerator(getContext());
        generator.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_marker_red_32px, null));
    }

    @OnClick(R.id.detail_event_fab)
    public void onClickSaveFab() {
        presenter.onSaveClick();
    }

    @OnClick(R.id.event_map_button_open_map)
    public void onClickOpenMap() {
        presenter.onOpenMapClick(location);
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_event_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
/*        if (itemId == R.id.toolbar_action_add_to_calendar) {
            presenter.onCalendarAddClick();
        }*/
        return true;
    }

    @Override
    public void showImage(String url) {
        if (url != null) {
            Picasso.with(getContext())
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(image);
        } else {
            // FIXME: 05.12.2016
        }
    }

    @Override
    public void showDescription(String desc, String name) {
        expandableTextView.setText(desc);
        eventName.setText(name);
        toolbar.setTitle(name);
    }

    @Override
    public void showWhen(String dateFrom, String dateTo) {
        durationTime.setText(DateUtil.toDuration(DateUtil.toDate(dateFrom), DateUtil.toDate(dateTo)));
    }

    @Override
    public void showLocation(Location location) {
        if (location == null) {
            street.setText(R.string.field_no_location);
            countryAndCity.setVisibility(View.GONE);
            cardViewMap.setVisibility(View.GONE);
            return;
        }
        countryAndCity.setText(getResources()
                .getString(R.string.location_country_city_format,
                        location.getCity(), location.getCountry())
        );
        if (location.getStreet() != null && !location.getStreet().isEmpty()) {
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
    public void showCategory(String category) {
        if (category == null || category.isEmpty()) {
            this.category.setText(R.string.field_no_category);
            return;
        }
        this.category.setText(category);
    }

    @Override
    public void showMessageAdded() {
        Snackbar.make(coordinatorLayout, R.string.message_event_added_saved, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_saved, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigationManager.goToFavorites();
                    }
                })
                .setActionTextColor(ContextCompat.getColor(getContext(), R.color.textAction))
                .show();
    }

    @Override
    public void addToCalendar() {

    }

    @Override
    public void openGoogleMapApp(LatLng latLng) {
        NavigationManager.openGoogleMapApp(getContext(), latLng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_maps_style);
        map.setMapStyle(style);

        if (location != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL));
            MarkerOptions marker =
                    new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon()));
            marker.position(location);
            map.addMarker(marker);
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
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
        unbinder.unbind();
    }
}
