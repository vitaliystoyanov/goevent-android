package com.stoyanov.developer.goevent;

import android.content.Context;

import com.orm.SugarApp;
import com.stoyanov.developer.goevent.di.component.ApplicationComponent;
import com.stoyanov.developer.goevent.di.component.DaggerApplicationComponent;
import com.stoyanov.developer.goevent.di.module.ApplicationModule;

public class MainApplication extends SugarApp {
    private ApplicationComponent applicationComponent;

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((MainApplication)context.getApplicationContext()).applicationComponent; // FIXME: 09.10.2016
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
