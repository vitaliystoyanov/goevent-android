package com.stoyanov.developer.goevent.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.CircularPropagation;
import android.transition.Fade;
import android.transition.SidePropagation;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.manager.LocationManager;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.utill.transition.PropagatingTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

public class MainFragment extends Fragment implements MainView {
    private Unbinder unbinder;
    private ActionBarDrawerToggle drawerToggle;
    @Inject
    NavigationManager navigationManager;
    @Inject
    LocationManager locationManager;
    @Inject
    MainPresenter presenter;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.root_content)
    ConstraintLayout rootContent;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    private SlidePagerAdapter pagerAdapter;
    private List<Event> data;
    private int pagerPosition;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((ContainerActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        setupToolbar();
        setupRecycleView();
        setupViewPager();
        tabs.addTab(tabs.newTab().setText("What's new"));
        tabs.addTab(tabs.newTab().setText("Top events"));
        tabs.addTab(tabs.newTab().setText("Channels"));
    }

    private void setupViewPager() {
        pagerAdapter = new SlidePagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
//        indicator.setViewPager(pager);
//        pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    private void setupRecycleView() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rvCategory.setLayoutManager(manager);
    }

    @OnClick(R.id.btn_location)
    public void onClickBtnLocation() {
        navigationManager.goToDefineLocation(getActivity());
    }

    @OnClick(R.id.btn_near_me)
    public void onClickBtnNearMe() {
        navigationManager.goToNearby();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        if (data == null) {
            presenter.load(locationManager.getLastDefinedLocation());
        } else {
            presenter.restore(data);
            pager.setCurrentItem(pagerPosition);
        }
        drawerToggle.syncState();
        ((ContainerActivity) getActivity()).setNavigationItem(R.id.drawer_item_main);
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.title_explore);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((ContainerActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((ContainerActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void showCategories(Set<Category> categories) {
        List<CategoryAdapter.Item> items = new ArrayList<>();
        for (Category c : categories)
            items.add(CategoryAdapter.Item.newBuilder().name(c.getName()).build());
        rvCategory.setAdapter(new CategoryAdapter(items, getContext()));
    }

    @Override
    public void showPopularEvents(List<Event> data) {
        this.data = data;
        pagerAdapter.setEvents(data);
        pagerAdapter.setEvents(data);
    }

    @Override
    public void showEmpty() {
    }

    @Override
    public void showProgress(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        rootContent.setVisibility(!isLoading ? View.VISIBLE : View.INVISIBLE);
        appbar.setExpanded(!isLoading, !isLoading);
        appbar.setActivated(!isLoading);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isLoading) {
            TransitionSet set = new TransitionSet();
            set.setPropagation(new SidePropagation());
            set.addTransition(new Fade(Fade.IN).setDuration(300));
            set.addTransition(new Slide(Gravity.BOTTOM));
            new PropagatingTransition(rootContent, pager, set,
                    650, new LinearInterpolator(), new CircularPropagation()).start();
        }
    }

    @Override
    public void showError() {
        Snackbar.make(getView(),
                R.string.message_bad_connection,
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroyView() {
        ((ContainerActivity) getActivity()).removeDrawerLayoutListener(drawerToggle);
        pagerPosition = pager.getCurrentItem();
        unbinder.unbind();
        super.onDestroyView();
    }

    private class SlidePagerAdapter extends FragmentPagerAdapter {
        private List<Event> events;

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setEvents(List<Event> events) {
            this.events = events;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return EventSliderFragment.newInstance(events.get(position));
        }

        @Override
        public float getPageWidth(int position) {
            return events.size() == 1 ? 1f : 0.93f;
        }

        @Override
        public int getCount() {
            return events != null ? events.size() : 0;
        }
    }
}
