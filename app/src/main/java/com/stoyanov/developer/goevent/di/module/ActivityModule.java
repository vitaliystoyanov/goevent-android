package com.stoyanov.developer.goevent.di.module;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.di.scope.ActivityScope;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private MainActivity mainActivity;

    public ActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    FragmentManager provideFragmentManager(MainActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @Provides
    @ActivityScope
    NavigationManager provideNavigationManager(FragmentManager manager) {
        return new NavigationManager(manager);
    }

    @Provides
    @ActivityScope
    LoaderManager provideLoaderManager(MainActivity activity) {
        return activity.getSupportLoaderManager();
    }
}
