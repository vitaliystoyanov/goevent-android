package com.stoyanov.developer.goevent.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.ActivityComponent;
import com.stoyanov.developer.goevent.di.component.DaggerActivityComponent;
import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.mvp.presenter.MainPresenter;
import com.stoyanov.developer.goevent.mvp.view.MainView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements MainView {
    private static final String TAG = "MainActivity";
    @Inject
    NavigationManager navigationManager;
    private MainPresenter presenter;
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(); // FIXME: 10/16/16 to dagger
        setupDagger();
        setupNavigationDrawer();
    }

    public void setupNavigationDrawer() {
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                drawerLayout.closeDrawers();
                int i = menuItem.getItemId();
                if (i == R.id.drawer_item_list_events) {
                    presenter.onItemListOfEvents();
                } else if (i == R.id.drawer_item_map) {
                    presenter.onItemMap();
                } else if (i == R.id.drawer_item_feedback) {
                    presenter.onItemAbout();
                } else if (i == R.id.drawer_item_login) {
                    presenter.onItemLogin();
                } else if (i == R.id.drawer_item_nearby) {
                    presenter.onItemNearby();
                } else if (i == R.id.drawer_item_notification) {
                    presenter.onItemNotifications();
                } else if (i == R.id.drawer_item_favorites) {
                    presenter.onItemFavorites();
                }
                return true;
            }
        });
//        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
//                toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupDagger() {
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent((MainApplication.getApplicationComponent(this)))
                .activityModule(new ActivityModule(this))
                .build();
        activityComponent.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attach(this);
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void onBackPressed() {
        navigationManager.navigateBack(this);
    }

    @Override
    public void goToMap() {
        navigationManager.goToMapEvents();
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
    public void goToAbout() {
        navigationManager.goToAbout();
    }

    @Override
    public void goToNotification() {
        navigationManager.goToNotifications();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

}
