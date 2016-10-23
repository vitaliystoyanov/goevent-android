package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orm.SugarRecord;
import com.stoyanov.developer.goevent.mvp.model.domain.FavoriteEvent;

import java.util.List;

public class FavoritesEventsLocalStorageImp implements FavoritesEventsStorage {

    @Override
    public void add(@NonNull FavoriteEvent event) {
        SugarRecord.save(event);
    }

    @Override
    public void remove(@NonNull FavoriteEvent event) {
        SugarRecord.delete(event);
    }

    @Nullable
    @Override
    public List<FavoriteEvent> get() {
        return SugarRecord.listAll(FavoriteEvent.class);
    }
}
