package com.stoyanov.developer.goevent.mvp.data.source.cache;

import android.support.annotation.Nullable;

import java.util.List;

public interface Cache<V> {

    boolean cachedAvailable();

    @Nullable List<V> getCached();

    boolean isCacheValid();

    void cache(List<V> values);

}
