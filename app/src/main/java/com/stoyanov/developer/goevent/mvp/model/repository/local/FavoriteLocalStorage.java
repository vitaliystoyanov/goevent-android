package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.SavedEvent;

import java.util.List;

public interface FavoriteLocalStorage {

    void add(@NonNull SavedEvent event);

    void remove(@NonNull SavedEvent event);

    @NonNull
    SavedEvent findSavedEvent(@NonNull SavedEvent event);

    @Nullable
    List<SavedEvent> get();

}
