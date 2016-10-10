package com.stoyanov.developer.goevent.ui.activity;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.ActivityComponent;
import com.stoyanov.developer.goevent.di.component.DaggerActivityComponent;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MainActivity";
    @Inject
    EventsRepository eventsRepository;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigationDrawer(toolbar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setupDagger();
    }

    private void setupNavigationDrawer(final Toolbar toolbar) {
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) { // FIXME: 08.10.2016 write simply
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_item_list_events:
                        Toast.makeText(getApplicationContext(), "List of events", Toast.LENGTH_SHORT).show();
                    case R.id.drawer_map:
                        Toast.makeText(getApplicationContext(), "Map", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupDagger() {
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationComponent((MainApplication.getApplicationComponent(this)))
                .build();
        activityComponent.inject(this);
        Log.d(TAG, "setupDagger: is cache null -> " + (eventsRepository.getCacheEvents() == null));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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
            onTest();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onTest() {
/*        List<Event> eventsTest = remoteDataSource.getEventsByLocation(50.4501f, 30.5234f);
        Toast.makeText(this, "size: " + eventsTest.size(), Toast.LENGTH_SHORT).show();*/
        new EventsAsyncTask().execute();
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

    public class EventsAsyncTask extends AsyncTask<Void, Void, List<Event>> {

        private final EventsRemoteDataSource remoteDataSource;

        public EventsAsyncTask() {
            remoteDataSource =
                    new EventsRemoteDataSource(getApplication());
        }

        @Override
        protected List<Event> doInBackground(Void... voids) {
            return remoteDataSource.getEvents();
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            Toast.makeText(MainActivity.this, "size of list: " + events.size(), Toast.LENGTH_SHORT).show();
        }
    }
}
