package com.stoyanov.developer.goevent.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.stoyanov.developer.goevent.mvp.model.repository.remote.UriBuilder;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MainActivity";
    @Inject
    EventsRepository eventsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setupDagger();
    }

    private void setupDagger() {
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((MainApplication) getApplication()).getApplicationComponent())
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
        EventsRemoteDataSource remoteDataSource =
                new EventsRemoteDataSource(new UriBuilder(getString(R.string.host),
                        getString(R.string.port)), getApplication());
        List<Event> eventsTest = remoteDataSource.getEventsByLocation(50.4501f, 30.5234f);
        Toast.makeText(this, "size: " + eventsTest.size(), Toast.LENGTH_SHORT).show();
    }
}
