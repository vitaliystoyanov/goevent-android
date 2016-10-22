package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
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
    private View root;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.fragment_list_of_events, null);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        DaggerFragmentComponent.builder()
                .activityComponent(((MainActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("List of events");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        presenter.attach(this);
        setupRecycleView();
        progressBar = (ProgressBar) getActivity().findViewById(R.id.fragment_events_progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity()
                .findViewById(R.id.fragment_events_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        presenter.onStart();
    }

    private void setupRecycleView() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_events_recycler_view);
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
        Log.d(TAG, "onStop: ");
        presenter.detach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.toolbar_actions_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.toolbar_action_test) {

        }
        return true;
    }

    @Override
    public void showEvents(List<Event> events) {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.addData(events);
//        if (events != null) {
//            Snackbar.make(root, "Shown events: " + events.size(), Snackbar.LENGTH_LONG).show();
//        }
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showProgressBar(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showMessageOnNotReceiveRemote() {
        Snackbar.make(root, "Check your connection or try again later", Snackbar.LENGTH_LONG).show();
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
