package com.stoyanov.developer.goevent.ui.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationSuggestion;
import com.stoyanov.developer.goevent.service.FetchAddressIntentService;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DefaultLocationActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "DefaultLocationActivity";
    private static final int REQUEST_PERMISSIONS = 1;
    public static final int REQUEST_CODE = 2;
    public static final int RUSULT_CODE_IF_NEEDED_UPDATE = 3;
    @BindView(R.id.default_location_floating_search_view)
    FloatingSearchView searchView;
    @BindView(R.id.rv_history_locations)
    RecyclerView recyclerView;
    @Inject
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private ResultReceiver suggestionAddressResultReceiver;
    private ResultReceiver addressResultReceiver;
    private boolean isConnectedGoogleApi;
    private String lastQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_location);
        ButterKnife.bind(this);
        (GoeventApplication.getApplicationComponent(this)).inject(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new historyLocationsAdapter(getResources()
//                .getStringArray(R.array.items_popular_locations)));

        searchView.setOnHomeActionClickListener(
                () -> {
                    if (searchView.isSearchBarFocused()) {
                        searchView.clearSearchFocus();
                    } else {
                        finish();
                    }
                });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchView.showProgress();
                lastQuery = searchSuggestion.getBody();
                FetchAddressIntentService.start(DefaultLocationActivity.this,
                        addressResultReceiver, lastQuery);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                searchView.showProgress();
                lastQuery = currentQuery;
                FetchAddressIntentService.start(DefaultLocationActivity.this,
                        addressResultReceiver, lastQuery);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
            }

            @Override
            public void onFocusCleared() {
                searchView.setSearchBarTitle(lastQuery != null ? lastQuery : "");
            }
        });
        searchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            if (!oldQuery.equals("") && newQuery.equals("")) {
                searchView.clearSuggestions();
                searchView.hideProgress();
            } else {
                FetchAddressIntentService.start(DefaultLocationActivity.this, suggestionAddressResultReceiver, newQuery);
                searchView.showProgress();
            }
        });
        searchView.setOnBindSuggestionCallback((suggestionView, leftIcon, textView, item, itemPosition) ->
                leftIcon.setImageResource(R.drawable.ic_marker_gray_24px));

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
                    LocationPref location = new LocationPref(address.getLatitude(),
                            address.getLongitude());
                    location.setCity(address.getLocality());
                    location.setCountry(address.getCountryName());

                    setResult(RUSULT_CODE_IF_NEEDED_UPDATE);
                    finish();

                    Log.d(TAG, "onReceiveResult: DefinedLocation - " + location.toString());
                    locationManager.updateLastDefinedLocation(location);
                } else if (resultCode == FetchAddressIntentService.Constants.FAILURE_RESULT) {
                    String errorMessage = resultData.getString(FetchAddressIntentService.Constants.ERROR_MESSAGE);
                    Log.d(TAG, "onReceiveResult: error - " + errorMessage);
                    searchView.clearSuggestions();
                }
                searchView.hideProgress();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @OnClick(R.id.cv_use_current_location)
    public void onClickUseCurrentLocation() {
        requirePermissions();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private void fetchMyCurrentLocation() {
        if (!isConnectedGoogleApi) return;
        @SuppressLint("MissingPermission")
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (location != null && Geocoder.isPresent()) {
            Toast.makeText(this, "Your location is lat: " + location.getLatitude() +
                    ", lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            FetchAddressIntentService.start(this, addressResultReceiver, location);
        }
    }

    private void requirePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
        } else {
            fetchMyCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            requirePermissions();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnectedGoogleApi = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        isConnectedGoogleApi = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isConnectedGoogleApi = false;
    }

    private static class historyLocationsAdapter extends RecyclerView.Adapter<historyLocationsAdapter.ViewHolder> {
        private String[] locations;

        public historyLocationsAdapter(@NonNull String[] locations) {
            this.locations = locations;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_popular_location, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.location.setText(locations[position]);
        }

        @Override
        public int getItemCount() {
            return locations.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView location;

            public ViewHolder(View v) {
                super(v);
                location = (TextView) v.findViewById(R.id.item_popular_location);
            }
        }
    }
}
