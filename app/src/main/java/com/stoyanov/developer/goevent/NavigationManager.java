package com.stoyanov.developer.goevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.activity.LoginActivity;
import com.stoyanov.developer.goevent.ui.activity.SettingsActivity;
import com.stoyanov.developer.goevent.ui.fragment.DetailEventFragment;
import com.stoyanov.developer.goevent.ui.fragment.FavoritesFragment;
import com.stoyanov.developer.goevent.ui.fragment.FeedBackFragment;
import com.stoyanov.developer.goevent.ui.fragment.ListOfEventsFragment;
import com.stoyanov.developer.goevent.ui.fragment.NearbyEventsFragment;

public class NavigationManager {
    private static final String TAG = "NavigationManager";

    private FragmentManager manager;

    public NavigationManager(FragmentManager manager) {
        this.manager = manager;
    }

    private void open(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.toString())
                .commit();
    }

    private void openAsRoot(Fragment fragment) {
        popEveryFragment();
        open(fragment);
    }

    private void popEveryFragment() {
        int backStackCount = manager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = manager.getBackStackEntryAt(i).getId();
            manager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void navigateBack(Activity baseActivity) {
        if (manager.getBackStackEntryCount() == 1) {
            baseActivity.finish();
        } else {
            manager.popBackStackImmediate();
        }
    }

    public void back(Fragment fragment) {
        if (manager.getBackStackEntryCount() != 1) {
            manager.popBackStackImmediate();
        }
    }

    public void goToListOfEvents() {
        openAsRoot(new ListOfEventsFragment());
    }

    public void goToAbout() {
        openAsRoot(new FeedBackFragment());
    }

    public void goToLoginForm(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void goToFavorites() {
        openAsRoot(new FavoritesFragment());
    }

    public void goToNearby() {
        openAsRoot(new NearbyEventsFragment());
    }

    public void goToDetailEvent(Event event) {
        open(DetailEventFragment.newInstance(event));
    }

    public void goToSearchEvents(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void goToNotificationSettings(Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }
}
