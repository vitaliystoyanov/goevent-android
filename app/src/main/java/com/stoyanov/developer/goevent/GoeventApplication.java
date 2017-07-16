package com.stoyanov.developer.goevent;

import android.app.Application;
import android.content.Context;

import com.stoyanov.developer.goevent.di.component.ApplicationComponent;
import com.stoyanov.developer.goevent.di.component.DaggerApplicationComponent;
import com.stoyanov.developer.goevent.di.module.ApplicationModule;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.RushCore;
import timber.log.Timber;

public class GoeventApplication extends Application {
    private ApplicationComponent applicationComponent;

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((GoeventApplication) context.getApplicationContext()).applicationComponent; // FIXME: 09.10.2016
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
        RushCore.initialize(config);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
