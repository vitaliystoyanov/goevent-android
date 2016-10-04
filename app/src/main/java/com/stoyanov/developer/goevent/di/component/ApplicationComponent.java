package com.stoyanov.developer.goevent.di.component;

import android.app.Application;

import com.stoyanov.developer.goevent.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Application application();

}
