package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.FavoriteEvent;

import java.util.List;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class FavoritesEventsLocalStorageImp implements FavoritesEventsStorage {

    @Override
    public void add(@NonNull FavoriteEvent event) {
        RushCore.getInstance().save(event);
    }

    @Override
    public void remove(@NonNull FavoriteEvent event) {
        RushCore.getInstance().delete(event);
    }

    @Nullable
    @Override
    public List<FavoriteEvent> get() {
        return new RushSearch().find(FavoriteEvent.class);
    }
}
