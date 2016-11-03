package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.FavoriteEvent;
import com.stoyanov.developer.goevent.mvp.model.domain.SuccessLogout;
import com.stoyanov.developer.goevent.mvp.model.domain.Token;

import java.util.List;

public interface UserBackendService {

    @Nullable
    Token login();

    @Nullable
    SuccessLogout logout();

    List<FavoriteEvent> getUserFavoriteEvent();

    void addFavoriteEvent(FavoriteEvent event);

}
