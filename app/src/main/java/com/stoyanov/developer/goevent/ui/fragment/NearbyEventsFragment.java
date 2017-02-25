package com.stoyanov.developer.goevent.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;
import com.stoyanov.developer.goevent.FetchAddressIntentService;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.DefinedLocation;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationSuggestion;
import com.stoyanov.developer.goevent.mvp.presenter.NearbyEventsPresenter;
import com.stoyanov.developer.goevent.mvp.view.NearbyEventsView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;
import com.stoyanov.developer.goevent.ui.common.EventMarkerClusterRenderer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NearbyEventsFragment extends Fragment
        implements NearbyEventsView, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_CHECK_SETTINGS = 1;
    private static final String TAG = "NearbyEventsFragment";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    @Inject
    NearbyEventsPresenter presenter;
    @Inject
    LocationManager locationManager;
    @BindView(R.id.nearby_events_mapview)
    MapView mapView;
    @BindView(R.id.nearby_floating_search_view)
    FloatingSearchView searchView;
    @BindView(R.id.nearby_events_viewpager)
    ViewPager viewPager;
    boolean isConnectedGoogleApi;
    private boolean isLocationSettingsSatisfied;
    private GoogleMap map;
    private Unbinder unbinder;
    private ClusterManager<Event> clusterManager;
    private GoogleApiClient googleApiClient;
    private DefinedLocation lastUserLocation;
    private ResultReceiver suggestionAddressResultReceiver;
    private ResultReceiver addressResultReceiver;
    private String lastQuery;
    private SlidePagerAdapter slidePagerAdapter;
    private Marker pointerPositionMarker;


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
        searchView.attachNavigationDrawerToMenuButton(((MainActivity) getActivity()).getDrawerLayout());
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {

            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.floating_search_my_location) {
                    presenter.onActionMenuMyLocation();
                }
            }
        });
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                    searchView.hideProgress();
                } else {
                    FetchAddressIntentService.start(getActivity(), suggestionAddressResultReceiver, newQuery);
                    searchView.showProgress();
                }
            }
        });
        searchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item,
                                         int itemPosition) {
                leftIcon.setImageResource(R.drawable.ic_marker_gray_24px);
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchView.showProgress();
                lastQuery = searchSuggestion.getBody();
                FetchAddressIntentService.start(getActivity(),
                        addressResultReceiver, lastQuery);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                searchView.showProgress();
                lastQuery = currentQuery;
                FetchAddressIntentService.start(getActivity(),
                        addressResultReceiver, lastQuery);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
            }

            @Override
            public void onFocusCleared() {
                if (lastQuery != null) searchView.setSearchBarTitle(lastQuery);
            }
        });

        suggestionAddressResultReceiver = new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Log.d(TAG, "onReceiveResult: Result code  - " + resultCode);
                if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                    List<Address> definedAddresses = resultData.getParcelableArrayList(FetchAddressIntentService.Constants.RESULT_DATA_ADDRESSES);
                    searchView.swapSuggestions(LocationSuggestion.create(definedAddresses));
                } else if (resultCode == FetchAddressIntentService.Constants.FAILURE_RESULT) {
                    String errorMessage = resultData.getString(FetchAddressIntentService.Constants.ERROR_MESSAGE);
                    Log.d(TAG, "onReceiveResult: error - " + errorMessage);
                    searchView.clearSuggestions();
                }
                searchView.hideProgress();
            }
        };
        addressResultReceiver = new ResultReceiver(new Handler()) {

            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Log.d(TAG, "onReceiveResult: Result code  - " + resultCode);
                if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                    List<Address> definedAddresses = resultData.getParcelableArrayList(FetchAddressIntentService.Constants.RESULT_DATA_ADDRESSES);
                    Address address = definedAddresses.get(0);

                    presenter.onUpdateSearchLocation(new DefinedLocation(address.getLatitude(),
                            address.getLongitude()));
                } else if (resultCode == FetchAddressIntentService.Constants.FAILURE_RESULT) {
                    String errorMessage = resultData.getString(FetchAddressIntentService.Constants.ERROR_MESSAGE);
                    Log.d(TAG, "onReceiveResult: error - " + errorMessage);
                    searchView.clearSuggestions();
                }
                searchView.hideProgress();
            }
        };
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        slidePagerAdapter = new SlidePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(slidePagerAdapter);
        viewPager.setClipToPadding(true);
        viewPager.setPageMargin(10);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isMapReady()) {
                    presenter.onPageSelected(slidePagerAdapter.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupDagger() {
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
    }

    @Override
    public void showMarkers(List<Event> events) {
        if (events == null) return;
        clusterManager.clearItems();
        clusterManager.addItems(events);
        clusterManager.cluster();
        slidePagerAdapter.removeAndAdd(events);
        if (events.size() == 0) Snackbar.make(mapView, "Here are no events.",
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
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
//                LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
                if (status.getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
                    isLocationSettingsSatisfied = true;
                } else if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        locationSettingsResult.getStatus().startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else if (status.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    isLocationSettingsSatisfied = false;
                    Toast.makeText(getContext(), "SETTINGS_CHANGE_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (isLocationSettingsSatisfied) {
            checkManifestPermissionsAndGetLocation();
        }
    }

    private void checkManifestPermissionsAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkManifestPermissionsAndGetLocation: null");
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Log.d(TAG, "onResult: location != null");
            presenter.onUpdateSearchLocation(new DefinedLocation(location.getLatitude(),
                    location.getLongitude()));
            Log.d(TAG, "onResult: Your location is: " + location.getLongitude()
                    + ", " + location.getLatitude());
        }
    }

    public LocationRequest createLocationRequest() {
        LocationRequest request = new LocationRequest();
        request.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        request.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode - " + requestCode);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "User agreed to make required location settings changes.");
                    isLocationSettingsSatisfied = true;
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "User chose not to make required location settings changes.");
                    break;
            }
        }
    }

    @Override
    public void updateMapCamera(DefinedLocation location, boolean isCurrentLocation) {
        LatLng position = new LatLng(location.getLatitude(),
                location.getLongitude());
        if (pointerPositionMarker != null) pointerPositionMarker.remove();
        IconGenerator generator = new IconGenerator(getContext());
        generator.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_marker_accent_32px, null));
        MarkerOptions pointerMakerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon()));
        pointerMakerOptions.position(position);
        pointerPositionMarker = map.addMarker(pointerMakerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        clusterManager = new ClusterManager<>(getActivity(), map);
        clusterManager.setRenderer(new EventMarkerClusterRenderer(getContext(), map, clusterManager));
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Event>() {
            @Override
            public boolean onClusterClick(Cluster<Event> cluster) {
                slidePagerAdapter.removeAndAdd(new ArrayList<>(cluster.getItems()));
                return false;
            }
        });
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Event>() {
            @Override
            public boolean onClusterItemClick(Event event) {
                slidePagerAdapter.singleItem(event);
                presenter.onClusterItemClick(event);
                return false;
            }
        });
        map.setOnMarkerClickListener(clusterManager);
        map.setOnCameraChangeListener(clusterManager);

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_maps_style);
        map.setMapStyle(style);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        lastUserLocation = locationManager.getLastDefinedLocation();
        if (lastUserLocation != null) { // FIXME: 1/2/17
            LatLng cameraPosition = new LatLng(lastUserLocation.getLatitude(),
                    lastUserLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 13));
        }
        presenter.onMapReady(lastUserLocation);
    }

    private boolean isMapReady() {
        return map != null;
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
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

    private class SlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Event> events;

        SlidePagerAdapter(FragmentManager fm) {
            super(fm);
            events = new ArrayList<>();
        }

        void removeAndAdd(List<Event> data) {
            events.clear();
            events.addAll(data);
            notifyDataSetChanged();
        }

        void clear() {
            events.clear();
            notifyDataSetChanged();
        }

        void singleItem(Event event) {
            events.clear();
            events.add(event);
            notifyDataSetChanged();
        }

        @Nullable
        public Event get(int position) {
            return events.size() != 0 ? events.get(position) : null;
        }

        @Override
        public Fragment getItem(int position) {
            return EventSlidePageFragment.newInstance(events.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public float getPageWidth(int position) {
            return events.size() == 1 ? 1f : 0.93f;
        }

        @Override
        public int getCount() {
            return events.size();
        }
    }
}
