package com.stoyanov.developer.goevent.mvp.model.domain;

import android.location.Address;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

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

    public static List<LocationSuggestion> create(List<Address> addresses) {
        List<LocationSuggestion> suggestions = new ArrayList<>();
        for (Address item : addresses) {
            StringBuilder builder = new StringBuilder();
            if (item.getThoroughfare() != null) {
                builder.append(item.getThoroughfare());
                builder.append(", ");
            }
            if (item.getLocality() != null) {
                builder.append(item.getLocality());
                builder.append(", ");
            }
            builder.append(item.getCountryName());
            suggestions.add(new LocationSuggestion(builder.toString()));
        }
        return suggestions;
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
