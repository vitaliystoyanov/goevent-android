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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.like.LikeButton;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.LocationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.DefinedLocation;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.presenter.EventsPresenter;
import com.stoyanov.developer.goevent.mvp.view.EventsView;
import com.stoyanov.developer.goevent.ui.activity.ContainerActivity;
import com.stoyanov.developer.goevent.ui.adapter.EventsAdapter;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import au.com.dardle.widget.BadgeLayout;

public class EventsFragment extends Fragment implements EventsView,
        BadgeLayout.OnBadgeClickedListener {
    private static final String TAG = "EventsFragment";
    @Inject
    EventsPresenter presenter;
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
    private DefinedLocation definedLocation;
    private RelativeLayout noUpcomingEventsLayout;

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
                .activityComponent(((ContainerActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        noUpcomingEventsLayout = (RelativeLayout) getActivity().findViewById(R.id.list_events_no_upcoming_events);
        badgeLayout = (BadgeLayout) getActivity().findViewById(R.id.list_events_badge_layout);
        badgeLayout.addOnBadgeClickedListener(this);
        coordinatorLayout = (CoordinatorLayout) getView().findViewById(R.id.list_of_events_coordinator_layout);
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.list_events_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationManager.goToAddEvent();
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
                ((ContainerActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((ContainerActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
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

        definedLocation = locationManager.getLastDefinedLocation();
        if (definedLocation != null)
            ((TextView) getView().findViewById(R.id.toolbar_location_textview))
                    .setText(definedLocation.getCity() + ", " + definedLocation.getCountry());

        presenter.onStart(locationManager.getLastDefinedLocation()); // FIXME: 26.02.2017
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
    public void onResume() {
        super.onResume();
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
        ((ContainerActivity) getActivity()).removeDrawerLayoutListener(drawerToggle);
    }

    @Override
    public void onBadgeClicked(BadgeLayout.Badge badge) {

    }

    @Override
    public void showCategories(Set<Category> categories) {
        for (Category cat : categories) {
            badgeLayout.addBadge(badgeLayout.newBadge().setText(cat.getName()));
        }
    }

    @Override
    public void showEvents(List<Event> events) {
        Log.d(TAG, "showSaved: Loaded events: " + events.size());
        noUpcomingEventsLayout.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.removeAndAdd(events);
    }

    @Override
    public void showEmpty() {
        noUpcomingEventsLayout.setVisibility(View.VISIBLE);
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
