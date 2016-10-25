package com.stoyanov.developer.goevent.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.presenter.FavoritesPresenter;
import com.stoyanov.developer.goevent.mvp.view.FavoritesView;
import com.stoyanov.developer.goevent.ui.activity.MainActivity;
import com.stoyanov.developer.goevent.ui.adapter.EventsAdapter;

import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesView {

    private ActionBarDrawerToggle drawerToggle;
    private EventsAdapter adapter;
    private ProgressBar progressBar;
    private FavoritesPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        setupRecycleView();
        presenter = new FavoritesPresenter();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.favorites_toolbar);
        toolbar.setTitle(R.string.toolbar_title_favorites);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.favorites_progress_bar);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((MainActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((MainActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
    }

    private void setupRecycleView() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.favorites_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventsAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        presenter.onStart();
        drawerToggle.syncState();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        ((MainActivity) getActivity()).removeDrawerLayoutListener(drawerToggle);
    }

    @Override
    public void showProgressBar(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showEvents(List<Event> events) {
        adapter.removeAndAdd(events);
        if (events != null) {
            Snackbar.make(getView(), "Shown events: " + events.size(), Snackbar.LENGTH_LONG).show();
        }
    }
}
