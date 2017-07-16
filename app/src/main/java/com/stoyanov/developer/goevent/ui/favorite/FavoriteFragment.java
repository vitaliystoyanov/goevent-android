package com.stoyanov.developer.goevent.ui.favorite;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.like.LikeButton;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.ui.events.EventsAdapter;

import java.util.List;

import javax.inject.Inject;

public class FavoriteFragment extends Fragment implements FavoriteView {
    private static final String TAG = "FavoriteFragment";
    @Inject
    FavoriteEventsPresenter presenter;
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
        return inflater.inflate(R.layout.fragment_favorite, null);
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
        toolbar.setTitle(R.string.title_favorite_events);
    }

    private void setupRecycleView() {
        RecyclerView list = (RecyclerView) getActivity().findViewById(R.id.saved_events_recycler_view);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView rvList, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        presenter.deleteItem(adapter.getItems().get(viewHolder.getAdapterPosition()));
                        adapter.remove(viewHolder.getAdapterPosition());
                        adapter.notifyDataSetChanged();
                        if (adapter.getItemCount() == 0) emptyLayout.setVisibility(View.VISIBLE);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list);

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
            }
        });
        list.setAdapter(adapter);
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
    public void removeItem(Event event, int itemPosition) {
        adapter.remove(itemPosition);
        if (adapter.getItemCount() == 0) emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSaved(List<Event> events) {
        if (emptyLayout.getVisibility() == View.VISIBLE) emptyLayout.setVisibility(View.INVISIBLE);
        adapter.removeAndAdd(events);
    }

    @Override
    public void goToDetailEvent(Event event) {
        navigationManager.goToDetailEvent(event);
    }
}
