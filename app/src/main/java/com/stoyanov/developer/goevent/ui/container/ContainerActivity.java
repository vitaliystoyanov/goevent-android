package com.stoyanov.developer.goevent.ui.container;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.ActivityComponent;
import com.stoyanov.developer.goevent.di.component.DaggerActivityComponent;
import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.ui.location.DefaultLocationActivity;
import com.stoyanov.developer.goevent.utill.Formatter;

import javax.inject.Inject;

public class ContainerActivity extends AppCompatActivity implements ContainerView {
    @Inject
    NavigationManager navigationManager;
    @Inject
    LocationManager locationManager;
    private ContainerPresenter presenter;
    private ActivityComponent activityComponent;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int resultCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        presenter = new ContainerPresenter(); // FIXME: 10/16/16 to dagger
        setupDagger();
        setupNavigationDrawer();
        presenter.attach(this);
        if (savedInstanceState == null) {
            if (locationManager.getLastDefinedLocation() == null) {
                navigationManager.goToDefineLocation(this);
            } else {
                presenter.openHome();
            }
        }
    }

    public void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            int i = menuItem.getItemId();
            if (navigationView.getMenu().findItem(i).isChecked()) return true;
            if (i == R.id.drawer_item_list_events) {
                presenter.onItemListOfEvents();
            } else if (i == R.id.drawer_item_login) {
                presenter.onItemLogin();
            } else if (i == R.id.drawer_item_nearby) {
                presenter.onItemNearby();
            } /*else if (i == R.id.drawer_item_notification) {
                presenter.onItemNotifications();
            }*/ else if (i == R.id.drawer_item_favorites) {
                presenter.onItemFavorites();
            } else if (i == R.id.drawer_item_defined_location) {
                presenter.onItemDefineLocation();
            } else if (i == R.id.drawer_item_main) {
                presenter.onItemMain();
            }
            return true;
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

    public void setNavigationItem(@IdRes int id) {
        navigationView.setCheckedItem(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DefaultLocationActivity.REQUEST_CODE
                && resultCode == DefaultLocationActivity.RUSULT_CODE_IF_NEEDED_UPDATE) {
            this.resultCode = resultCode;
        } else {
            navigationManager.delegateOnActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationPref location = locationManager.getLastDefinedLocation();
        if (location != null) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.drawer_item_defined_location);
            item.setTitle(Formatter.formatLocation(location));
        }

        if (resultCode != 0) {
            presenter.onItemMain();
            resultCode = 0;
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
    public void goToMain() {
        navigationManager.goToHome();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
