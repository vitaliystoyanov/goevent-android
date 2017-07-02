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
import android.widget.RelativeLayout;

import com.like.LikeButton;
import com.stoyanov.developer.goevent.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.presenter.SavedEventsPresenter;
import com.stoyanov.developer.goevent.mvp.view.SavedEventsView;
import com.stoyanov.developer.goevent.ui.activity.ContainerActivity;
import com.stoyanov.developer.goevent.ui.adapter.EventsAdapter;

import java.util.List;

import javax.inject.Inject;


public class SavedEventsFragment extends Fragment implements SavedEventsView {
    private static final String TAG = "SavedEventsFragment";
    @Inject
    SavedEventsPresenter presenter;
    @Inject
    NavigationManager navigationManager;
    private ActionBarDrawerToggle drawerToggle;
    private EventsAdapter adapter;
    private ProgressBar progressBar;
    private RelativeLayout emptyLayout;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_events, null);
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
        emptyLayout = (RelativeLayout) getActivity().findViewById(R.id.saved_events_empty_layout);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.saved_events_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.saved_events_progress_bar);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((ContainerActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((ContainerActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
    }

    private void setupRecycleView() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.saved_events_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventsAdapter(getContext(), new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                presenter.onItemClick(adapter.getItem(position));
            }
        }, new EventsAdapter.OnLikeItemClickListener() {
            @Override
            public void onItem(int position) {
                presenter.onItem(adapter.getItem(position), position);
            }

            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                presenter.onUnlike();
            }
        });
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
        ((ContainerActivity) getActivity()).removeDrawerLayoutListener(drawerToggle);
    }

    @Override
    public void showProgressBar(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void removeItemFromList(final Event event, int itemPosition) {
        adapter.remove(itemPosition);
        if (adapter.getItemCount() == 0) emptyLayout.setVisibility(View.VISIBLE);
/*        Snackbar.make(getView(),
                "Event was removed",
                Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.onUndoClick(event);
                    }
                })
                .show();*/
    }

    @Override
    public void showEmpty() {
        emptyLayout.setVisibility(View.VISIBLE);
        toolbar.setTitle(R.string.toolbar_title_no_saved_events);
    }

    @Override
    public void showSaved(List<Event> events) {
        if (emptyLayout.getVisibility() == View.VISIBLE) emptyLayout.setVisibility(View.INVISIBLE);
        toolbar.setTitle(getString(R.string.toolbar_title_saved_events, events.size()));
        adapter.removeAndAdd(events);
//        Snackbar.make(getView(), "Synchronization...", Snackbar.LENGTH_SHORT).show();
        Snackbar.make(getView(), "Please, log in to update saved events", Snackbar.LENGTH_LONG)
                .setAction("Log in", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }

    @Override
    public void goToDetailEvent(Event event) {
        navigationManager.goToDetailEvent(event);
    }
}
