package com.stoyanov.developer.goevent.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.eventdetail.EventDetailFragment;
import com.stoyanov.developer.goevent.ui.events.EventsFragment;
import com.stoyanov.developer.goevent.ui.favorite.FavoriteFragment;
import com.stoyanov.developer.goevent.ui.location.DefaultLocationActivity;
import com.stoyanov.developer.goevent.ui.signin.SignInActivity;
import com.stoyanov.developer.goevent.ui.main.MainFragment;
import com.stoyanov.developer.goevent.ui.nearby.NearbyEventsFragment;
import com.stoyanov.developer.goevent.ui.settings.SettingsActivity;

public class NavigationManager extends BaseNavigationManager {
    private NearbyEventsFragment nearbyEventsFragment;

    public NavigationManager(FragmentManager manager) {
        super(manager);
    }

    public static void openGoogleMapApp(Context context, LatLng location) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location.latitude + "," + location.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public void goToHome() {
        openAsRoot(MainFragment.newInstance());
    }

    public void goToListOfEvents() {
        open(new EventsFragment());
    }

    public void goToLoginForm(Context context) {
        context.startActivity(new Intent(context, SignInActivity.class));
    }

    public void goToFavorites() {
        openAsRoot(new FavoriteFragment());
    }

    public void goToNearby() {
        if (nearbyEventsFragment == null) nearbyEventsFragment = new NearbyEventsFragment();
        openAsRoot(new NearbyEventsFragment());
    }

    public void goToDetailEvent(Event event) {
        open(EventDetailFragment.newInstance(event));
    }

    public void goToDetailEvent(Event event, ImageView sharedImageView, String transitionName) {
        getManager().beginTransaction()
                .addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView))
                .replace(R.id.container, EventDetailFragment.newInstance(event, transitionName))
                .addToBackStack(null)
                .commit();
    }

    public void goToSearchEvents(Context context) {
        context.startActivity(new Intent(context, SignInActivity.class));
    }

    public void goToNotificationSettings(Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }

    public void goToDefineLocation(Activity activity) {
        activity.startActivityForResult(new Intent(activity, DefaultLocationActivity.class), DefaultLocationActivity.REQUEST_CODE);
    }

    public void goToAddEvent() {
    }

    public void delegateOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NearbyEventsFragment.REQUEST_CHECK_SETTINGS) {
            if (nearbyEventsFragment != null)
                nearbyEventsFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
