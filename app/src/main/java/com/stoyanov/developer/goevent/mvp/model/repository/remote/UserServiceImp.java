package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.SavedEvent;
import com.stoyanov.developer.goevent.mvp.model.domain.SuccessLogout;
import com.stoyanov.developer.goevent.mvp.model.domain.Token;

import java.util.List;

public class UserServiceImp implements UserService {

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
    public List<SavedEvent> getUserFavoriteEvent() {
        return null;
    }

    @Override
    public void addFavoriteEvent(SavedEvent event) {

    }
}
