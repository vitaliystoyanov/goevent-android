package com.stoyanov.developer.goevent.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.stoyanov.developer.goevent.R;

public class SearchActivity extends AppCompatActivity {

    private FloatingSearchView floatingSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_events_toolbar);
        setSupportActionBar(toolbar);
        floatingSearchView = (FloatingSearchView) findViewById(R.id.search_events_floating_search_view);
        floatingSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                finish();
            }
        });
    }

}
