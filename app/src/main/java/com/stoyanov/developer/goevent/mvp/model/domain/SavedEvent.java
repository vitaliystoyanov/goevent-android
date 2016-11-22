package com.stoyanov.developer.goevent.mvp.model.domain;

import java.util.Date;

import co.uk.rushorm.core.RushObject;

public class SavedEvent extends RushObject {
    private String picture;
    private String eventId;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private EventLocation eventLocation;
    private Date createdDate;

    public SavedEvent() {
    }

    public SavedEvent(Event event) {
        picture = event.getPicture();
        description = event.getDescription();
        eventId = event.getEventId();
        name = event.getName();
        startTime = event.getStartTime();
        endTime = event.getEndTime();
        eventLocation = new EventLocation(event.getLocation());
    }

    public Event toEvent() {
        return new Event(picture, eventId, name, description, startTime, endTime, eventLocation);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    public EventLocation getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(EventLocation eventLocation) {
        this.eventLocation = eventLocation;
    }
}
