package com.stoyanov.developer.goevent.mvp.data.source.cache;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CacheInMemory<K, V extends Identification<K>> implements Cache<V> {

    private Map<K, V> cachedMap;
    private boolean cacheIsValid;

    @Override
    public boolean cachedAvailable() {
        return cachedMap != null && cacheIsValid;
    }

    @Nullable
    @Override
    public List<V> getCached() {
        return cachedMap == null ? null : new ArrayList<>(cachedMap.values());
    }

    @Override
    public boolean isCacheValid() {
        return cacheIsValid;
    }

    @Override
    public void cache(List<V> values) {
        if (values == null) {
            cachedMap = null;
            cacheIsValid = true;
            return;
        }
        if (cachedMap == null) {
            cachedMap = new LinkedHashMap<>();
        }
        cachedMap.clear();
        for (V element : values) {
            cachedMap.put(element.getId(), element);
        }
        cacheIsValid = true;
    }

    public void cache(V value) {
        if (cachedMap == null) {
            cachedMap = new LinkedHashMap<>();
        }
        cachedMap.put(value.getId(), value);
    }
}
