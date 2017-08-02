package com.stoyanov.developer.goevent.ui.container;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.ActivityComponent;
import com.stoyanov.developer.goevent.di.component.DaggerActivityComponent;
import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;

import javax.inject.Inject;

public class ContainerActivity extends AppCompatActivity implements ContainerView {
    private static final String TAG = "ContainerActivity";
    @Inject
    NavigationManager navigationManager;
    @Inject
    LocationManager locationManager;
    private ContainerPresenter presenter;
    private ActivityComponent activityComponent;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        presenter = new ContainerPresenter(); // FIXME: 10/16/16 to dagger
        setupDagger();
        setupNavigationDrawer();
    }

    public void setupNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
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
                } else if (i == R.id.drawer_item_home) {
                    presenter.onItemHome();
                }
                return true;
            }
        });
    }

    private void setupDagger() {
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent((GoeventApplication.getApplicationComponent(this)))
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
        presenter.attach(this);
        presenter.openHome();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationPref location = locationManager.getLastDefinedLocation();
        if (location != null) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.drawer_item_defined_location);
            item.setTitle(location.getCity());
        }
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

    @Override
    public void goToHome() {
        navigationManager.goToHome();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
