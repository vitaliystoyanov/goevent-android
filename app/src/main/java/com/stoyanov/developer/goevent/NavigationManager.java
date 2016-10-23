package com.stoyanov.developer.goevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stoyanov.developer.goevent.ui.activity.LoginActivity;
import com.stoyanov.developer.goevent.ui.activity.SearchActivity;
import com.stoyanov.developer.goevent.ui.fragment.AboutFragment;
import com.stoyanov.developer.goevent.ui.fragment.ListEventsFragment;
import com.stoyanov.developer.goevent.ui.fragment.MapEventsFragment;

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

    public void goToListOfEvents() {
        openAsRoot(new ListEventsFragment());
    }

    public void goToMapEvents() {
        openAsRoot(new MapEventsFragment());
    }

    public void goToAbout() {
        openAsRoot(new AboutFragment());
    }

    public void goToLoginForm(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void goToFavorites() {

    }

    public void goToNotifications() {

    }

    public void goToNearby() {

    }

    public void goToSearchEvents(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }
}
