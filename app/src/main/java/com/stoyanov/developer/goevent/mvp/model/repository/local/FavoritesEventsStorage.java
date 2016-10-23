package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.FavoriteEvent;

import java.util.List;

public interface FavoritesEventsStorage {

    void add(@NonNull FavoriteEvent event);

    void remove(@NonNull FavoriteEvent event);

    @Nullable
    List<FavoriteEvent> get();

}
