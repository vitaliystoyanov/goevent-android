package com.stoyanov.developer.goevent.di.component;

import com.stoyanov.developer.goevent.di.module.ApplicationModule;
import com.stoyanov.developer.goevent.di.module.EventsRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, EventsRepositoryModule.class})
public interface ApplicationComponent {



}
