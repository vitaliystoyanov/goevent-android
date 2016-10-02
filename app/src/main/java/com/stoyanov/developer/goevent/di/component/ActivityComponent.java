package com.stoyanov.developer.goevent.di.component;

import com.stoyanov.developer.goevent.di.scope.ActivityScope;

import dagger.Component;


@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent {

}
