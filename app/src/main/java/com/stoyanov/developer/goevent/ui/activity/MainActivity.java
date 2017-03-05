package com.stoyanov.developer.goevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.stoyanov.developer.goevent.LocationPreferences;
import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.ActivityComponent;
import com.stoyanov.developer.goevent.di.component.DaggerActivityComponent;
import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.presenter.MainPresenter;
import com.stoyanov.developer.goevent.mvp.view.MainView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = "MainActivity";
    @Inject
    NavigationManager navigationManager;
    private MainPresenter presenter;
    private ActivityComponent activityComponent;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(); // FIXME: 10/16/16 to dagger
        setupDagger();
        setupNavigationDrawer();
        Log.d(TAG, "onCreate: ");
        savedInstance = savedInstanceState;
    }

    public void setupNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                drawerLayout.closeDrawers();
                int i = menuItem.getItemId();
                if (i == R.id.drawer_item_list_events) {
                    presenter.onItemListOfEvents();
                } else if (i == R.id.drawer_item_login) {
                    presenter.onItemLogin();
                } else if (i == R.id.drawer_item_nearby) {
                    presenter.onItemNearby();
                } /*else if (i == R.id.drawer_item_notification) {
                    presenter.onItemNotifications();
                }*/ else if (i == R.id.drawer_item_saved) {
                    presenter.onItemFavorites();
                } else if (i == R.id.drawer_item_defined_location) {
                    presenter.onItemDefineLocation();
                }
                return true;
            }
        });
    }

    private void setupDagger() {
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent((MainApplication.getApplicationComponent(this)))
                .activityModule(new ActivityModule(this))
                .build();
        activityComponent.inject(this);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayoutListener(DrawerLayout.DrawerListener listener) {
        drawerLayout.addDrawerListener(listener);
    }

    public void removeDrawerLayoutListener(DrawerLayout.DrawerListener listener) {
        drawerLayout.removeDrawerListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        navigationManager.delegateOnActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        presenter.attach(this);
        presenter.onStart();
        if (savedInstance != null) {
            Log.d(TAG, "onStart: savedInstanceState != null");
            navigationManager.restoreFragmentState(savedInstance);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        presenter.detach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        LocationPref location =  LocationPreferences.get();
        if (location != null) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.drawer_item_defined_location);
            item.setTitle(location.getCity() + ", " + location.getCountry());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        navigationManager.saveFragmentState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        navigationManager.navigateBack(this);
    }

    @Override
    public void goToListOfEvents() {
        navigationManager.goToListOfEvents();
    }

    @Override
    public void goToLogin() {
        navigationManager.goToLoginForm(this);
    }

    @Override
    public void goToFavorites() {
        navigationManager.goToFavorites();
    }

    @Override
    public void goToNearby() {
        navigationManager.goToNearby();
    }

    @Override
    public void goToDefineLocation() {
        navigationManager.goToDefineLocation(this);
    }

    @Override
    public void goToNotificationSettings() {
        navigationManager.goToNotificationSettings(this);
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
