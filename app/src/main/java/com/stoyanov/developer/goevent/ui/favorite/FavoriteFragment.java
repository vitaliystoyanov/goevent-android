package com.stoyanov.developer.goevent.ui.favorite;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.like.LikeButton;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.ui.events.EventsAdapter;
import com.stoyanov.developer.goevent.utill.transition.PropagatingTransition;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FavoriteFragment extends Fragment implements FavoriteView {
    @Inject
    FavoriteEventsPresenter presenter;
    @Inject
    NavigationManager navigationManager;
    @BindView(R.id.saved_events_recycler_view)
    RecyclerView savedEventsRecyclerView;
    private ActionBarDrawerToggle drawerToggle;
    private EventsAdapter adapter;
    private ProgressBar progressBar;
    private ConstraintLayout emptyLayout;
    private Unbinder unbinder;
    private List<Event> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, null);
        unbinder = ButterKnife.bind(this, v);
        return v;
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
        emptyLayout = getActivity().findViewById(R.id.empty_layout);
    }

    private void setupToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.saved_events_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        progressBar = getActivity().findViewById(R.id.saved_events_progress_bar);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((ContainerActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((ContainerActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
        toolbar.setTitle(R.string.title_favorite_events);
    }

    private void setupRecycleView() {
        RecyclerView list = getActivity().findViewById(R.id.saved_events_recycler_view);
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

        adapter = new EventsAdapter(getContext(), (position, sharedImageView, transitionName) ->
                presenter.onItemClick(adapter.getItem(position), sharedImageView, transitionName),
                new EventsAdapter.OnLikeItemClickListener() {
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
        if (data == null) {
            presenter.load();
        } else {
            presenter.restore(data);
        }
        drawerToggle.syncState();
        ((ContainerActivity) getActivity()).setNavigationItem(R.id.drawer_item_favorites);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @OnClick(R.id.btn_explore_more_events)
    public void onClickBtnExploreMore() {
        navigationManager.goToListOfEvents();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        ((ContainerActivity) getActivity()).removeDrawerLayoutListener(drawerToggle);
        unbinder.unbind();
        super.onDestroyView();
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
    public void show(List<Event> events) {
        this.data = events;
        if (emptyLayout.getVisibility() == View.VISIBLE) emptyLayout.setVisibility(View.INVISIBLE);
        adapter.removeAndAdd(events);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet set = new TransitionSet();
            set.setPropagation(new SidePropagation());
            set.addTransition(new Fade(Fade.IN).setDuration(300));
            set.addTransition(new Slide(Gravity.BOTTOM));
            new PropagatingTransition(savedEventsRecyclerView, savedEventsRecyclerView, set,
                    650, new LinearInterpolator(), new CircularPropagation()).start();
        }
    }

    @Override
    public void goToDetailEvent(Event event, ImageView sharedImageView, String transitionName) {
        navigationManager.goToDetailEvent(event, sharedImageView, transitionName);
    }
}
