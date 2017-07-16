package com.stoyanov.developer.goevent.manager;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stoyanov.developer.goevent.R;

public class BaseNavigationManager {
    private FragmentManager manager;

    public BaseNavigationManager(FragmentManager manager) {
        this.manager = manager;
    }

    public static void runReplaceTransaction(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.toString())
                .commit();
    }

    public void open(Fragment fragment) {
        runReplaceTransaction(manager, fragment);
    }

    public void openAsRoot(Fragment fragment) {
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
}
