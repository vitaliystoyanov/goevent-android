package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.di.module.PresenterModule;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.presenter.ListEventsPresenter;
import com.stoyanov.developer.goevent.mvp.view.ListEventsView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;
import com.stoyanov.developer.goevent.ui.adapter.EventsAdapter;

import java.util.List;

import javax.inject.Inject;

public class ListEventsFragment extends Fragment implements ListEventsView {
    private static final String TAG = "ListEventsFragment";
    @Inject
    ListEventsPresenter presenter;
    private ProgressBar progressBar;
    private EventsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_events, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupRecycleView();
        progressBar = (ProgressBar) getActivity().findViewById(R.id.fragment_events_progress_bar);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getActivity()
                .findViewById(R.id.fragment_events_swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        presenter.attach(this);
        presenter.onStart();
    }

    private void setupRecycleView() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.events_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventsAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void showEvents(List<Event> events) {
        adapter.setData(events);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showProgressBar(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
    }
}
