package com.stoyanov.developer.goevent.di.component;

import com.stoyanov.developer.goevent.di.scope.ActivityScope;

import dagger.Component;


@Component(dependencies = ApplicationComponent.class)
@ActivityScope
public interface MainActivityComponent {

}
