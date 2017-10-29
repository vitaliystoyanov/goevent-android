package com.stoyanov.developer.goevent.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.eventdetail.EventDetailFragment;
import com.stoyanov.developer.goevent.ui.events.EventsFragment;
import com.stoyanov.developer.goevent.ui.favorite.FavoriteFragment;
import com.stoyanov.developer.goevent.ui.location.DefaultLocationActivity;
import com.stoyanov.developer.goevent.ui.login.LoginActivity;
import com.stoyanov.developer.goevent.ui.main.MainFragment;
import com.stoyanov.developer.goevent.ui.nearby.NearbyEventsFragment;
import com.stoyanov.developer.goevent.ui.settings.SettingsActivity;

public class NavigationManager extends BaseNavigationManager {
    private NearbyEventsFragment nearbyEventsFragment;

    public NavigationManager(FragmentManager manager) {
        super(manager);
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
        Fragment fragment = EventDetailFragment.newInstance(event, "");
        runReplaceTransaction(manager, fragment);
    }

    public void goToListOfEvents() {
        openAsRoot(new EventsFragment());
    }

    public void goToLoginForm(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void goToFavorites() {
        openAsRoot(new FavoriteFragment());
    }

    public void goToNearby() {
        openAsRoot(new NearbyEventsFragment());
    }

    public void goToDetailEvent(Event event, ImageView sharedImageView, String transitionName) {
        getManager().beginTransaction()
                .addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                .replace(R.id.container, EventDetailFragment.newInstance(event, transitionName))
                .addToBackStack(null)
                .commit();
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

    public void goToHome() {
        open(MainFragment.newInstance());
    }
}
