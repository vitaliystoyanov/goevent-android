package com.stoyanov.developer.goevent.di.module;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.di.scope.ActivityScope;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private ContainerActivity containerActivity;

    public ActivityModule(ContainerActivity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Provides
    @ActivityScope
    ContainerActivity provideMainActivity() {
        return containerActivity;
    }

    @Provides
    @ActivityScope
    FragmentManager provideFragmentManager(ContainerActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @Provides
    @ActivityScope
    NavigationManager provideNavigationManager(FragmentManager manager) {
        return new NavigationManager(manager);
    }

    @Provides
    @ActivityScope
    LoaderManager provideLoaderManager(ContainerActivity activity) {
        return activity.getSupportLoaderManager();
    }
}
