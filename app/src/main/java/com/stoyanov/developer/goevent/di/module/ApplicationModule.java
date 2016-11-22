package com.stoyanov.developer.goevent.di.module;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(application.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
