package com.stoyanov.developer.goevent.mvp.model.domain;

import java.util.List;

public final class Events {
    private List<Event> events;
    private int count;

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
        return "EventsLocalStorage{" +
                "count=" + count +
                ", [array size]=" + events.size() +
                ", events=" + builder.toString() +
                '}';
    }
}
