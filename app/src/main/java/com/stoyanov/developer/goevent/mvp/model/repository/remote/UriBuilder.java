package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

public class UriBuilder {
    private static final String API_VERSION = "v1.0";
    private static final String PORT = ":8000";
    private final String authority;

    public UriBuilder(String authority) {
        this.authority = authority;
    }

    @NonNull
    public String getEvents() {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("http")
                .encodedAuthority(authority + PORT)
                .appendPath(API_VERSION)
                .appendPath("events")
                .build().toString();
    }

    @NonNull
    public String getEvent(@NonNull String id) {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("http")
                .encodedAuthority(authority + PORT)
                .appendPath(API_VERSION)
                .appendPath("events")
                .appendPath(id)
                .build().toString();
    }
}
