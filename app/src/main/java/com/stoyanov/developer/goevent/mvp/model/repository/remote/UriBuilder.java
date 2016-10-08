package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

public class UriBuilder {
    private static final String API_VERSION = "v1.0";
    private final String authority;

    public UriBuilder(String authority, String port) {
        this.authority = authority + ":" + port;
    }

    @NonNull
    public String getEvents() {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("http")
                .encodedAuthority(authority)
                .appendPath(API_VERSION)
                .appendPath("events")
                .build().toString();
    }

    @NonNull
    public String getEvent(@NonNull String id) {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("http")
                .encodedAuthority(authority)
                .appendPath(API_VERSION)
                .appendPath("events")
                .appendPath(id)
                .build().toString();
    }

    @NonNull
    public String getEventsByLocation(float latitude, float longitude, int distance) {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("http")
                .encodedAuthority(authority)
                .appendPath(API_VERSION)
                .appendPath("events-location")
                .appendQueryParameter("lat", String.valueOf(latitude))
                .appendQueryParameter("lng", String.valueOf(longitude))
                .appendQueryParameter("distance", String.valueOf(distance))
                .build().toString();
    }

    @NonNull
    public String getEventsByLocation(float latitude, float longitude) {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("http")
                .encodedAuthority(authority)
                .appendPath(API_VERSION)
                .appendPath("events-location")
                .appendQueryParameter("lat", String.valueOf(latitude))
                .appendQueryParameter("lng", String.valueOf(longitude))
                .build().toString();
    }
}
