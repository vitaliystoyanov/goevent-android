package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.FavoriteEvent;
import com.stoyanov.developer.goevent.mvp.model.domain.SuccessLogout;
import com.stoyanov.developer.goevent.mvp.model.domain.Token;

import java.util.List;

public class UserBackendServiceImp implements UserBackendService {

    @Nullable
    @Override
    public Token login() {
        return null;
    }

    @Nullable
    @Override
    public SuccessLogout logout() {
        return null;
    }

    @Override
    public List<FavoriteEvent> getUserFavoriteEvent() {
        return null;
    }

    @Override
    public void addFavoriteEvent(FavoriteEvent event) {

    }
}
