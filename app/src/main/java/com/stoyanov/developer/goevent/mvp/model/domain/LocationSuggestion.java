package com.stoyanov.developer.goevent.mvp.model.domain;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class LocationSuggestion implements SearchSuggestion {

    public static final Creator<LocationSuggestion> CREATOR = new Creator<LocationSuggestion>() {
        @Override
        public LocationSuggestion createFromParcel(Parcel in) {
            return new LocationSuggestion(in);
        }

        @Override
        public LocationSuggestion[] newArray(int size) {
            return new LocationSuggestion[size];
        }
    };
    private String location;

    public LocationSuggestion(String location) {
        this.location = location;
    }

    public LocationSuggestion(Parcel parcel) {
        location = parcel.readString();
    }

    @Override
    public String getBody() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location);
    }
}
