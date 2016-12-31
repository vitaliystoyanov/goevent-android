package com.stoyanov.developer.goevent.mvp.model.repository.remote.api;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventsApi {

    @GET("events")
    Call<Events> getEvents();

    @GET("events/{id}")
    Call<Event> getEvent(@Path("id") String id);

    @GET("events-location")
    Call<Events> getEventsByLocation(@Query("lat") double latitude, @Query("lng") double longitude);

    @GET("events-location")
    Call<Events> getEventsByLocation(@Query("lat") double latitude, @Query("lng") double longitude,
                                     @Query("distance") float distance);

}
