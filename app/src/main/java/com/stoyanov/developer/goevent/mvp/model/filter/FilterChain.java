package com.stoyanov.developer.goevent.mvp.model.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterChain<T> {

    private List<Filter<T>> filters = new ArrayList<>();

    public FilterChain<T> add(Filter<T> filter) {
        filters.add(filter);
        return this;
    }

    public T execute(T data) {
        T handled = null;
        for (Filter<T> f : filters) {
            if (handled == null) {
                handled = f.handle(data);
            } else {
                handled = f.handle(handled);
            }
        }
        return handled;
    }
}
