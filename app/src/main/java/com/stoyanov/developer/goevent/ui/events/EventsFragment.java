package com.stoyanov.developer.goevent.ui.events;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.CircularPropagation;
import android.transition.Fade;
import android.transition.SidePropagation;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.like.LikeButton;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.utill.DateUtil;
import com.stoyanov.developer.goevent.utill.Formatter;
import com.stoyanov.developer.goevent.utill.transition.PropagatingTransition;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import au.com.dardle.widget.BadgeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventsFragment extends Fragment implements EventsView, DatePickerDialog.OnDateSetListener {
    @Inject
    EventsPresenter presenter;
    @Inject
    NavigationManager navigationManager;
    @Inject
    LocationManager locationManager;

    @BindView(R.id.list_of_events_coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.list_events_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.badge_date)
    BadgeLayout badgeDate;
    @BindView(R.id.list_events_fab)
    FloatingActionButton fab;

    private Map<String, BadgeLayout.Badge> mapBadgesDates;
    private Map<String, BadgeLayout.Badge> mapBadgesCategories;
    private Map<String, Boolean> mapBadgesCategoriesSelectedState;

    private Toolbar toolbar;
    private RecyclerView rvList;
    private BadgeLayout badgeCategories;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout noUpcomingEventsLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LocationPref location;
    private Unbinder unbinder;
    private EventsAdapter adapter;
    private String customDateRange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_events, null);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((ContainerActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
    }

    private void setupToolbar() {
        toolbar = getView().findViewById(R.id.list_events_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((ContainerActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((ContainerActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
    }

    private void setupEventsAdapter() {
        adapter = new EventsAdapter(getContext(), (position, sharedImageView, transitionName) -> {
            presenter.onItem(adapter.getItem(position), sharedImageView, transitionName);
        },
                new EventsAdapter.OnLikeItemClickListener() {

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
        rvList = getView().findViewById(R.id.list_events_recycle_view);
        rvList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

    private void setupToolbarTitle(Toolbar toolbar) {
        View toolbarContainer = LayoutInflater
                .from(getContext())
                .inflate(R.layout.toolbar_title, toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(toolbarContainer, lp);
        toolbarContainer.findViewById(R.id.toolbar_location_textview)
                .setOnClickListener(view -> navigationManager.goToDefineLocation(getActivity()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noUpcomingEventsLayout = getActivity().findViewById(R.id.list_events_no_upcoming_events);

        badgeCategories = getActivity().findViewById(R.id.badge_categories);
        setupBadges();

        FloatingActionButton fab = getView().findViewById(R.id.list_events_fab);
        fab.setOnClickListener(view1 -> navigationManager.goToAddEvent());
        setupToolbar();
        setupEventsAdapter();
        setupToolbarTitle(toolbar);
        setupRecycleView();

        swipeRefreshLayout = getActivity().findViewById(R.id.list_events_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setDistanceToTriggerSync(50);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.onRefresh();
            swipeRefreshLayout.setRefreshing(true);
        });

        location = locationManager.getLastDefinedLocation();
        if (location != null) {
            ((TextView) getView().findViewById(R.id.toolbar_location_textview))
                    .setText(Formatter.formatLocation(location));
        }
        presenter.attach(this);
        presenter.provideData(locationManager.getLastDefinedLocation());
    }

    private void setupBadges() {
        mapBadgesDates = new LinkedHashMap<>();
        mapBadgesCategories = new LinkedHashMap<>();
        mapBadgesCategoriesSelectedState = new HashMap<>();
        mapBadgesDates.put(getString(R.string.title_today), badgeDate.newBadge().setText(getString(R.string.title_today)));
        mapBadgesDates.put(getString(R.string.title_tomorrow), badgeDate.newBadge().setText(getString(R.string.title_tomorrow)));
        mapBadgesDates.put(getString(R.string.title_weekend), badgeDate.newBadge().setText(getString(R.string.title_weekend)));
        mapBadgesDates.put(getString(R.string.title_custom_date), badgeDate.newBadge().setText(getString(R.string.title_custom_date)));
        for (BadgeLayout.Badge b : mapBadgesDates.values()) badgeDate.addBadge(b);
        badgeDate.addOnBadgeClickedListener(badge -> {
            if (badge.getText().equals(customDateRange) || badge.getText().equals(getString(R.string.title_custom_date))) {
                openDateRangePicker();
            }
            for (BadgeLayout.Badge b : mapBadgesDates.values()) {
                if (!badge.getText().equals(b.getText()))
                    b.setSelected(false);
            }
            badge.setSelected(true);
            selectByDate(badge.getText().toString());
        });

        badgeCategories.addOnBadgeClickedListener(badge -> {
            for (BadgeLayout.Badge b : mapBadgesCategories.values()) {
                b.setSelected(false);
                mapBadgesCategoriesSelectedState.put(b.getText().toString(), false);
            }
            mapBadgesCategoriesSelectedState.put(badge.getText().toString(), true);
            performFiltering();
            badge.setSelected(true);
        });
    }

    private void selectByDate(String name) {
        if (name.equals(getString(R.string.title_today))) {
            presenter.loadForToday();
        } else if (name.equals(getString(R.string.title_tomorrow))) {
            presenter.loadForTomorrow();
        } else if (name.equals(getString(R.string.title_weekend))) {
            presenter.loadForWeekend();
        }
    }

    private void performFiltering() {
        for (BadgeLayout.Badge badge : mapBadgesCategories.values()) {
            if (mapBadgesCategoriesSelectedState.get(badge.getText())) {
                if (!badge.getText().equals(getString(R.string.title_all))) {
                    adapter.getFilter().filter(badge.getText());
                } else {
                    adapter.getFilter().filter("");
                }
            }
        }
    }

    private void openDateRangePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAutoHighlight(true);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onStart() {
        super.onStart();
        drawerToggle.syncState();
        ((ContainerActivity) getActivity()).setNavigationItem(R.id.drawer_item_list_events);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        ((ContainerActivity) getActivity()).removeDrawerLayoutListener(drawerToggle);
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showCategories(Set<Category> categories) {
        badgeCategories.removeAllBadges();
        mapBadgesCategories.clear();
        mapBadgesCategoriesSelectedState.clear();
        BadgeLayout.Badge bAll = badgeCategories.newBadge().setText(getString(R.string.title_all)).setSelected(true);
        mapBadgesCategories.put(bAll.getText().toString(), bAll);
        badgeCategories.addBadge(bAll);
        for (Category cat : categories) {
            BadgeLayout.Badge b = badgeCategories.newBadge().setText(cat.getName());
            mapBadgesCategories.put(cat.getName(), b);
            mapBadgesCategoriesSelectedState.put(cat.getName(), false);
            badgeCategories.addBadge(b);
        }
    }

    @Override
    public void showEvents(List<Event> events) {
        noUpcomingEventsLayout.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.removeAndAdd(events);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet set = new TransitionSet();
            set.setPropagation(new SidePropagation());
            set.addTransition(new Fade(Fade.IN).setDuration(300));
            set.addTransition(new Slide(Gravity.BOTTOM));
            new PropagatingTransition(rvList, rvList, set,
                    650, new LinearInterpolator(), new CircularPropagation()).start();
        }
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
    public void showAddedToSaved() {
        Snackbar.make(coordinatorLayout, R.string.message_event_added_saved, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void goToSearchEvents() {
        navigationManager.goToSearchEvents(getContext());
    }

    @Override
    public void goToDetailEvent(Event event, ImageView sharedImageView, String transitionName) {
        navigationManager.goToDetailEvent(event, sharedImageView, transitionName);
    }

    @Override
    public void showProgress(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showError() {
        Snackbar.make(coordinatorLayout,
                R.string.message_bad_connection, Snackbar.LENGTH_LONG)
                .show();
        getActivity().runOnUiThread(() -> {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear,
                          int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar dateStart = Calendar.getInstance();
        Calendar dateEnd = Calendar.getInstance();
        dateStart.set(year, monthOfYear, dayOfMonth);
        dateEnd.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
        BadgeLayout.Badge b = mapBadgesDates.get(customDateRange);
        if (b == null) {
            b = mapBadgesDates.get(getString(R.string.title_custom_date));
        }
        Pair<String, String> r = DateUtil.toDurationWithoutTimeRange(dateStart.getTime(), dateEnd.getTime());
        customDateRange = String.format("%s: %s - %s", getString(R.string.title_date), r.first, r.second);
        b.setText(customDateRange);
        presenter.loadForCustomDateRange(r);
    }
}
