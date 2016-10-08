package com.stoyanov.developer.goevent.mvp.model.repository;

import android.support.annotation.Nullable;

import com.stoyanov.developer.goevent.mvp.model.domain.SuccessLogout;
import com.stoyanov.developer.goevent.mvp.model.domain.Token;

public interface UserDataSource {

    @Nullable
    Token login();

    @Nullable
    SuccessLogout logout();

}
