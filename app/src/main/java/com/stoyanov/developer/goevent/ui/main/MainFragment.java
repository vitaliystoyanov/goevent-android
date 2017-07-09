package com.stoyanov.developer.goevent.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepositoryImp;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsManager;
import com.stoyanov.developer.goevent.mvp.model.repository.local.EventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.local.SavedEventsLocalStorageImp;
import com.stoyanov.developer.goevent.mvp.model.repository.remote.EventsBackendServiceImp;
import com.stoyanov.developer.goevent.ui.activity.ContainerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment implements MainView {
    private Unbinder unbinder;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private CategoryAdapter adapter;

    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.viewpager)
    ViewPager pager;
    private SlidePagerAdapter pagerAdapter;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecycleView();
        setupViewPager();
    }

    private void setupViewPager() {
        pagerAdapter = new SlidePagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    private void setupRecycleView() {
        RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((ContainerActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((ContainerActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
        toolbar.setTitle(R.string.title_home);
    }

    @Override
    public void onStart() {
        super.onStart();
        drawerToggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<CategoryAdapter.Item> items = new ArrayList<>();
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concert").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Art Entertament").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Community organizations").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concert3434").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concert346666").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concert346756").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concerthtyjtjtyty").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concertt").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concerttyj").build());
        items.add(CategoryAdapter.Item.newBuilder().amount(10).name("Concerttj").build());
        rvCategory.setAdapter(new CategoryAdapter(items, getContext()));

        pagerAdapter.setEvents(new SavedEventsManager(new SavedEventsLocalStorageImp()).get());
        pagerAdapter.setEvents(new SavedEventsManager(new SavedEventsLocalStorageImp()).get()); // FIXME: 7/9/17 bug
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        public int getCount() {
            return events != null ? events.size() : 0;
        }
    }
}
