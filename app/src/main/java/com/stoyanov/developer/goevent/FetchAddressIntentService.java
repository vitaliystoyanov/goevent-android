package com.stoyanov.developer.goevent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIntentServi";
    private static final int MAX_RESULTS = 5;
    private static final int MAX_RESULTS_BEST_MATCH = 1;
    private ResultReceiver resultReceiver;

    public FetchAddressIntentService() {
        super(FetchAddressIntentService.class.getName());
    }

    public static void start(Context context, ResultReceiver receiver, Location latLng) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, receiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, latLng);
        context.startService(intent);
    }

    public static void start(Context context, ResultReceiver receiver, String locationName) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, receiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, locationName);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Location locationLatLng = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        String locationName = intent.getStringExtra(Constants.LOCATION_DATA_EXTRA);
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        String errorMessage = "";

        ArrayList<Address> addresses = null;
        try {
            if (locationLatLng != null) {
                addresses = new ArrayList<>(geocoder.getFromLocation(
                        locationLatLng.getLatitude(),
                        locationLatLng.getLongitude(),
                        MAX_RESULTS_BEST_MATCH));
            } else if (locationName != null) {
                addresses = new ArrayList<>(geocoder.getFromLocationName(locationName, MAX_RESULTS));
            }
        } catch (IOException ioException) {
            errorMessage = getString(R.string.geocoding_service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.geocoding_invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + locationLatLng.getLatitude() +
                    ", Longitude = " +
                    locationLatLng.getLongitude(), illegalArgumentException);
        }

        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.geocoding_no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Log.i(TAG, getString(R.string.geocoding_address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT, addresses);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ERROR_MESSAGE, message);
        resultReceiver.send(resultCode, bundle);
    }

    private void deliverResultToReceiver(int resultCode, ArrayList<Address> addresses) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.RESULT_DATA_ADDRESSES, addresses);
        resultReceiver.send(resultCode, bundle);
    }

    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME = "com.google.android.gms.location.locationaddress";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
        public static final String ERROR_MESSAGE = PACKAGE_NAME + ".ERROR_MESSAGE";
        public static final String RESULT_DATA_ADDRESSES = PACKAGE_NAME + ".RESULT_DATA_ADDRESSES";
    }
}
