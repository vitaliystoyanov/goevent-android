package com.stoyanov.developer.goevent.mvp.model.filter;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.filter.filters.StartDateFilter;

import java.util.List;

public class FilterManager {

    public static List<Event> filterByDateAndAllCategory(List<Event> events) {
        return new FilterChain<List<Event>>()
                .add(new StartDateFilter())
                .execute(events);
    }
}
