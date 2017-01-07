package com.stoyanov.developer.goevent.mvp.model.repository.remote.api;

import com.stoyanov.developer.goevent.mvp.model.domain.Login;
import com.stoyanov.developer.goevent.mvp.model.domain.SavedEvent;
import com.stoyanov.developer.goevent.mvp.model.domain.SuccessLogout;
import com.stoyanov.developer.goevent.mvp.model.domain.Token;

import java.util.List;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @POST("user/login")
    Call<Token> login(@Body Login login);

    @POST("user/logout")
    Call<SuccessLogout> logout();

    @GET("user/user-events")
    Call<List<SavedEvent>> getFavoriteEvents();

    @POST("user/new-event")
    Call<Response> addFavoriteEvents();

}
