package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stoyanov.developer.goevent.mvp.model.domain.SavedEvent;

import java.util.Date;
import java.util.List;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;

public class FavoriteEventLocalStorageImp implements FavoriteEventLocalStorage {
    private static final String TAG = "FavoritesEventsLocalSto";

    @Override
    public void add(@NonNull SavedEvent event) {
        event.setCreatedDate(new Date());
        RushCore.getInstance().save(event);
        Log.d(TAG, "add: favoriteEvent " + event.getId());
    }

    @Override
    public void remove(@NonNull SavedEvent event) {
        SavedEvent foundEvent = findSavedEvent(event);
        if (foundEvent != null) RushCore.getInstance().delete(foundEvent);
    }

    @NonNull
    @Override
    public SavedEvent findSavedEvent(@NonNull SavedEvent event) {
        return new RushSearch()
                .whereEqual("eventId", event.getEventId())
                .findSingle(SavedEvent.class);
    }

    @Nullable
    @Override
    public List<SavedEvent> get() {
        return new RushSearch().orderDesc("createdDate").find(SavedEvent.class);
    }
}
