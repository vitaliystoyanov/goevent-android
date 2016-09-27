package com.stoyanov.developer.goevent.di.module;

import android.content.Context;

import com.stoyanov.developer.goevent.MainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final MainApplication application;

    public ApplicationModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }
}
