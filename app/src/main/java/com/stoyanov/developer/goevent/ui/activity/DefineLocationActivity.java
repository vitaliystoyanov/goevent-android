package com.stoyanov.developer.goevent.ui.activity;

import android.Manifest;
import android.content.Intent;
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
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.stoyanov.developer.goevent.FetchAddressIntentService;
import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.LastDefinedLocation;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefineLocationActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "DefineLocationActivity";
    @BindView(R.id.define_location_floating_search_view)
    FloatingSearchView floatingSearchView;
    @Inject
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private AddressResultReceiver addressResultReceiver;
    private boolean isConnectedGoogleApi;
    private Location definedCurrentLocation;

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
        floatingSearchView.setOnHomeActionClickListener(
                new FloatingSearchView.OnHomeActionClickListener() {

                    @Override
                    public void onHomeClicked() {
                        finish();
                    }
                });
        floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.floating_search_my_location) {
                    fetchCurrentLocation();
                }
            }
        });
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Log.d(TAG, "onSearchAction: ");
                if (!currentQuery.isEmpty()) startFetchAddressIntentService(currentQuery);
                floatingSearchView.showProgress(); // FIXME: 12/31/16
            }
        });
        addressResultReceiver = new AddressResultReceiver(new Handler());
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

    private void startFetchAddressIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, addressResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private void startFetchAddressIntentService(String locationName) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, addressResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, locationName);
        startService(intent);
    }

    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!isConnectedGoogleApi) return;
        floatingSearchView.showProgress();
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (location != null) {
            definedCurrentLocation = location;
            Toast.makeText(this, "Your location is Lat: " + location.getLatitude() +
                    ", Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            if (Geocoder.isPresent()) {
                startFetchAddressIntentService(location);
            }
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
        Log.d(TAG, "onConnectionFailed: ");
        isConnectedGoogleApi = false;
    }

    public class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String addressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            Address definedAddress = resultData.getParcelable(FetchAddressIntentService.Constants.RESULT_DATA_ADDRESS);
            Toast.makeText(DefineLocationActivity.this, addressOutput, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onReceiveResult: Address found - " + addressOutput);
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT && definedAddress != null) {
                Log.d(TAG, "onReceiveResult: " + getString(R.string.geocoding_address_found));

                LastDefinedLocation location = new LastDefinedLocation(definedAddress.getLatitude(),
                        definedAddress.getLongitude());
                location.setCity(definedAddress.getLocality());
                location.setCountry(definedAddress.getCountryName());
                Log.d(TAG, "onReceiveResult: LastDefinedLocation - " + location.toString());
                locationManager.updateLastDefinedLocation(location);
                finish();
            }
            floatingSearchView.hideProgress();
        }
    }
}
