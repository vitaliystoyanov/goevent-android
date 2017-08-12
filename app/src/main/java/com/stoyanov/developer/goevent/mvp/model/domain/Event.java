package com.stoyanov.developer.goevent.mvp.model.domain;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import org.parceler.Parcel;

import co.uk.rushorm.core.RushObject;

@Parcel
public class Event extends RushObject implements ClusterItem {

    @SerializedName("eventPicture")
    private String picture;
    @SerializedName("eventId")
    private String eventId;
    @SerializedName("eventName")
    private String name;
    @SerializedName("eventDescription")
    private String description;
    @SerializedName("eventCategory")
    private String category;
    @SerializedName("eventStartTime")
    private String startTime;
    @SerializedName("eventEndTime")
    private String endTime;

    private EventLocation eventLocation;

    public Event() {
    }

    public Event(String picture, String eventId, String name, String description,
                 String startTime, String endTime, EventLocation eventLocation) {
        this.picture = picture;
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventLocation = eventLocation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Nullable
    public Location getLocation() {
        return eventLocation != null ? eventLocation.getLocation() : null;
    }

    public void setLocation(Location location) {
        this.eventLocation = new EventLocation(location);
    }

    @Override
    public String toString() {
        String locationToString = null;
        if (eventLocation != null) {
            locationToString = eventLocation.getLocation().toString();
        }
        return "Event{" +
                ", picture='" + picture + '\'' +
                ", eventId='" + eventId + '\'' +
                ", name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", eventLocation=" + locationToString +
                '}';
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
    }
}
