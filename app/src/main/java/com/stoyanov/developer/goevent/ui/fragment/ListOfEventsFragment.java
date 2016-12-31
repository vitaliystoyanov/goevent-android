package com.stoyanov.developer.goevent.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.like.LikeButton;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.LastDefinedLocation;
import com.stoyanov.developer.goevent.mvp.presenter.ListOfEventsPresenter;
import com.stoyanov.developer.goevent.mvp.view.ListOfEventsView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;
import com.stoyanov.developer.goevent.ui.adapter.EventsAdapter;

import java.util.List;

import javax.inject.Inject;

import au.com.dardle.widget.BadgeLayout;

public class ListOfEventsFragment extends Fragment implements ListOfEventsView,
        BadgeLayout.OnBadgeClickedListener {
    private static final String TAG = "ListOfEventsFragment";
    @Inject
    ListOfEventsPresenter presenter;
    @Inject
    NavigationManager navigationManager;
    @Inject
    LocationManager locationManager;
    private ProgressBar progressBar;
    private EventsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private BadgeLayout badgeLayout;
    private LastDefinedLocation lastDefinedLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_list_of_events, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        badgeLayout = (BadgeLayout) getActivity().findViewById(R.id.list_events_badge_layout);
        badgeLayout.addOnBadgeClickedListener(this);
        coordinatorLayout = (CoordinatorLayout) getView().findViewById(R.id.list_of_events_coordinator_layout);
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.list_events_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationManager.goToNearby();
            }
        });
        setupToolbar();
        setupEventsAdapter();
        setupToolbarTitle(toolbar);
        setupRecycleView();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) getView().findViewById(R.id.list_events_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((MainActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((MainActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
    }

    private void setupEventsAdapter() {
        adapter = new EventsAdapter(getContext(), new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                presenter.onItem(adapter.getItem(position));
            }
        }, new EventsAdapter.OnLikeItemClickListener() {

            @Override
            public void onItem(int position) {
                presenter.onItemStar(adapter.getItem(position));
            }

            @Override
            public void liked(LikeButton likeButton) {
                presenter.onLike();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                presenter.onUnlike();
            }
        });
    }

    private void setupRecycleView() {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.list_events_recycle_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbarTitle(Toolbar toolbar) {
        View toolbarContainer = LayoutInflater
                .from(getContext())
                .inflate(R.layout.toolbar_title, toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(toolbarContainer, lp);
        toolbarContainer.findViewById(R.id.toolbar_location_textview)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigationManager.goToDefineLocation(getContext());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        presenter.attach(this);
        drawerToggle.syncState();
        progressBar = (ProgressBar) getView().findViewById(R.id.list_events_progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity()
                .findViewById(R.id.list_events_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setDistanceToTriggerSync(50);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        lastDefinedLocation = locationManager.getLastDefinedLocation();
        if (lastDefinedLocation != null) ((TextView) getView().findViewById(R.id.toolbar_location_textview))
                .setText(lastDefinedLocation.getCity() + ", " + lastDefinedLocation.getCountry());
        presenter.onStart(lastDefinedLocation);
        Log.d(TAG, "onCreate: " + locationManager.toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.list_events_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.toolbar_action_search) {
//            presenter.onActionSearch();
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStop() {
        super.onStop();
        badgeLayout.removeOnBadgeClickedListener(this);
        presenter.detach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        ((MainActivity) getActivity()).removeDrawerLayoutListener(drawerToggle);
    }

    @Override
    public void onBadgeClicked(BadgeLayout.Badge badge) {

    }

    @Override
    public void showEvents(List<Event> events) {
        Log.d(TAG, "showSaved: Loaded events: " + events.size());
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.removeAndAdd(events);
    }

    @Override
    public void showEmpty() {
    }

    @Override
    public void clearEvents() {
        adapter.removeAll();
    }

    @Override
    public void showMessageAddedToFavorite() {
        Snackbar.make(coordinatorLayout, R.string.message_event_added_saved, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_saved, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigationManager.goToFavorites();
                    }
                })
                .setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorActionText))
                .show();
    }

    @Override
    public void goToSearchEvents() {
        navigationManager.goToSearchEvents(getContext());
    }

    @Override
    public void goToDetailEvent(Event event) {
        navigationManager.goToDetailEvent(event);
    }

    @Override
    public void showProgress(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showMessageNetworkError() {
        Snackbar.make(coordinatorLayout,
                R.string.message_bad_connection, Snackbar.LENGTH_LONG)
                .show();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
