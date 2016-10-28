package com.stoyanov.developer.goevent.mvp.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

@Table
public class Event implements Parcelable {

    public static final Parcelable.Creator<Event> CREATOR =
            new Parcelable.Creator<Event>() {
                @Override
                public Event createFromParcel(Parcel parcel) {
                    return new Event(parcel);
                }

                @Override
                public Event[] newArray(int i) {
                    return new Event[i];
                }
            };
    @SerializedName("eventPicture")
    private String picture;
    @SerializedName("eventId")
    private String eventId;
    @SerializedName("eventName")
    private String name;
    @SerializedName("eventDescription")
    private String description;
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

    private Event(Parcel in) {
        picture = in.readString();
        description = in.readString();
        eventId = in.readString();
        name = in.readString();
        startTime = in.readString();
        endTime = in.readString();
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

    public Location getLocation() {
        return eventLocation != null ? eventLocation.getLocation() : null;
    }

    public void setLocation(Location location) {
        this.eventLocation = new EventLocation(location);
    }

    @Override
    public String toString() {
        String locationToString = null;
        try {
            locationToString = eventLocation.getLocation().toString();
        } catch (NullPointerException e) {
            System.err.println(e);
        }
        return "Event{" +
                ", picture='" + picture + '\'' +
                ", eventId='" + eventId + '\'' +
                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", eventLocation=" + locationToString +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(picture);
        parcel.writeString(description);
        parcel.writeString(eventId);
        parcel.writeString(name);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public class EventLocation {
        private Location location;

        public EventLocation(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }
    }
}
