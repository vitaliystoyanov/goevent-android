package com.stoyanov.developer.goevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.activity.DefaultLocationActivity;
import com.stoyanov.developer.goevent.ui.activity.LoginActivity;
import com.stoyanov.developer.goevent.ui.activity.SettingsActivity;
import com.stoyanov.developer.goevent.ui.fragment.DetailEventFragment;
import com.stoyanov.developer.goevent.ui.fragment.EventsFragment;
import com.stoyanov.developer.goevent.ui.fragment.NearbyEventsFragment;
import com.stoyanov.developer.goevent.ui.fragment.SavedEventsFragment;

public class NavigationManager {
    private static final String TAG = "NavigationManager";

    private FragmentManager manager;
    private NearbyEventsFragment nearbyEventsFragment;

    public NavigationManager(FragmentManager manager) {
        this.manager = manager;
        nearbyEventsFragment = new NearbyEventsFragment();
    }

    public static void openGoogleMapApp(Context context, LatLng location) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location.latitude + "," + location.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static void goToDetailEvent(FragmentManager manager, Event event) {
        Fragment fragment = DetailEventFragment.newInstance(event);
        runReplaceTransaction(manager, fragment);
    }

    private static void runReplaceTransaction(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.toString())
                .commit();
    }

    private void open(Fragment fragment) {
        runReplaceTransaction(manager, fragment);
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
        openAsRoot(new EventsFragment());
    }

    public void goToLoginForm(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void goToFavorites() {
        openAsRoot(new SavedEventsFragment());
    }

    public void goToNearby() {
        openAsRoot(nearbyEventsFragment);
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

    public void goToDefineLocation(Context context) {
        context.startActivity(new Intent(context, DefaultLocationActivity.class));
    }

    public void goToAddEvent() {

    }

    public void delegateOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NearbyEventsFragment.REQUEST_CHECK_SETTINGS) {
            nearbyEventsFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
