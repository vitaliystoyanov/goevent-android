package com.stoyanov.developer.goevent.mvp.model.domain;

import com.stoyanov.developer.goevent.mvp.model.repository.cache.Identification;

import java.util.List;

public final class Events implements Identification<String> {
    private List<Event> events;
    private int count;

    @Override
    public String getId() { // FIXME: 07.10.2016
        return null;
    }

    public List<Event> list() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Event e :
                events) {
            builder.append(e.toString());
            builder.append("\n");
        }
        return "Events{" +
                "count=" + count +
                ", [array size]=" + events.size() +
                ", events=" + builder.toString() +
                '}';
    }
}
