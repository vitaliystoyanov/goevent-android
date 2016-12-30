package com.stoyanov.developer.goevent.mvp.model.filter;

public interface Filter<T> {

    T handle(T data);

}
