package com.stoyanov.developer.goevent.ui.activity;

import android.Manifest;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.stoyanov.developer.goevent.FetchAddressIntentService;
import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.DefinedLocation;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationSuggestion;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefineLocationActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "DefineLocationActivity";
    @BindView(R.id.define_location_floating_search_view)
    FloatingSearchView searchView;
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
        setContentView(R.layout.activity_define_location);
        ButterKnife.bind(this);
        (MainApplication.getApplicationComponent(this)).inject(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        searchView.setOnHomeActionClickListener(
                new FloatingSearchView.OnHomeActionClickListener() {

                    @Override
                    public void onHomeClicked() {
                        if (searchView.isSearchBarFocused()) {
                            searchView.clearSearchFocus();
                        } else {
                            finish();
                        }
                    }
                });
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.floating_search_my_location) {
                    fetchMyCurrentLocation();
                }
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchView.showProgress();
                lastQuery = searchSuggestion.getBody();
                FetchAddressIntentService.start(DefineLocationActivity.this,
                        addressResultReceiver, lastQuery);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                searchView.showProgress();
                lastQuery = currentQuery;
                FetchAddressIntentService.start(DefineLocationActivity.this,
                        addressResultReceiver, lastQuery);
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
//                List<LocationSuggestion> suggestions = new ArrayList<>();
//                suggestions.add(new LocationSuggestion("Kyiv, Ukraine"));
//                suggestions.add(new LocationSuggestion("Lviv, Ukraine"));
//                suggestions.add(new LocationSuggestion("Odessa, Ukraine"));
//                searchView.swapSuggestions(suggestions);
            }

            @Override
            public void onFocusCleared() {
                searchView.setSearchBarTitle(lastQuery);
            }
        });
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                    searchView.hideProgress();
                } else {
                    FetchAddressIntentService.start(DefineLocationActivity.this, suggestionAddressResultReceiver, newQuery);
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
                    DefinedLocation location = new DefinedLocation(address.getLatitude(),
                            address.getLongitude());
                    location.setCity(address.getLocality());
                    location.setCountry(address.getCountryName());

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

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private void fetchMyCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!isConnectedGoogleApi) return;
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (location != null && Geocoder.isPresent()) {
            Toast.makeText(this, "Your location is Lart: " + location.getLatitude() +
                    ", Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            FetchAddressIntentService.start(this, addressResultReceiver, location);
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
}
