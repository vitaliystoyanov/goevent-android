package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.SuccessLogout;

public interface UserDataSource {

    @Nullable
    String login();

    @Nullable
    SuccessLogout logout();

}
