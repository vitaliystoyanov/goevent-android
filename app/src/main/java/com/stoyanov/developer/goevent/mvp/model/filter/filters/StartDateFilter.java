package com.stoyanov.developer.goevent.mvp.model.filter.filters;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.filter.Comparators;
import com.stoyanov.developer.goevent.mvp.model.filter.Filter;

import java.util.Collections;
import java.util.List;

public class StartDateFilter implements Filter<List<Event>> {

    @Override
    public List<Event> handle(List<Event> data) {
        Collections.sort(data, new Comparators.EventsComparatorByDate());
        return data;
    }
}
