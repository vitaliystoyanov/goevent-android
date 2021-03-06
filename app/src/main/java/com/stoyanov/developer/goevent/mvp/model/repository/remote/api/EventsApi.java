package com.stoyanov.developer.goevent.mvp.model.repository.remote.api;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventsApi {

    @GET("events")
    Call<Events> getEvents();

    @GET("events/{id}")
    Single<Event> getEvent(@Path("id") String id);

    @GET("events-location")
    Observable<Events> getEventsByLocation(@Query("lat") double latitude,
                                           @Query("lng") double longitude);

    @GET("events-location")
    Single<Events> getEventsByLocation(@Query("lat") double latitude,
                                       @Query("lng") double longitude,
                                       @Query("distance") float distance);

    @GET("events-location")
    Single<Events> getEventsByLocation(@Query("lat") double latitude,
                                       @Query("lng") double longitude,
                                       @Query("distance") float distance,
                                       @Query("since") String since,
                                       @Query("until") String until);
}
