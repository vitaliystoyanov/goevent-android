package com.stoyanov.developer.goevent.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.ActivityComponent;
import com.stoyanov.developer.goevent.di.component.DaggerActivityComponent;
import com.stoyanov.developer.goevent.di.module.ActivityModule;
import com.stoyanov.developer.goevent.mvp.view.MainView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = "MainActivity";
    @Inject
    NavigationManager navigationManager;
    private ActionBarDrawerToggle drawerToggle;
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigationDrawer(toolbar);
        setupDagger();
        navigationManager.showListOfEvents();
    }

    private void setupNavigationDrawer(final Toolbar toolbar) {
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                drawerLayout.closeDrawers();
                int i = menuItem.getItemId();
                if (i == R.id.drawer_item_list_events) {
                    navigationManager.showListOfEvents(); // FIXME: 10/15/16 Move to presenter
                } else if (i == R.id.drawer_map) {
                    navigationManager.showMapEvents(); // FIXME: 10/15/16 Move to presenter
                }
                return true;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupDagger() {
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent((MainApplication.getApplicationComponent(this)))
                .activityModule(new ActivityModule(this))
                .build();
        activityComponent.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.toolbar_action_test) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        navigationManager.navigateBack(this);
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
