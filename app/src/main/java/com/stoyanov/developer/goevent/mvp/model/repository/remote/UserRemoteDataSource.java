package com.stoyanov.developer.goevent.mvp.model.repository.remote;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.SuccessLogout;
import com.stoyanov.developer.goevent.mvp.model.repository.UserDataSource;

public class UserRemoteDataSource implements UserDataSource {

    @Nullable
    @Override
    public String login() {
        return null;
    }

    @Nullable
    @Override
    public SuccessLogout logout() {
        return null;
    }
}
