package com.stoyanov.developer.goevent;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.fragment.ListEventsFragment;

import java.util.List;

public class NavigationManager {

    private FragmentManager manager;

    public NavigationManager(FragmentManager fragmentManager) {
        manager = fragmentManager;
    }

    private void open(Fragment fragment) {
        if (manager != null) {
            manager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(fragment.toString())
                    .commit();
        }
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

    public void showListOfEvents(List<Event> events) {
        ListEventsFragment fragment  = ListEventsFragment.newInstance();
        open(fragment);
    }
}
