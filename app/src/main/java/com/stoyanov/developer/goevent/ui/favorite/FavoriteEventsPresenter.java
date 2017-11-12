package com.stoyanov.developer.goevent.ui.favorite;

import android.content.Context;
import android.widget.ImageView;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

public class FavoriteEventsPresenter extends BasePresenter<FavoriteView> {
    @Inject
    FavoriteManager favoriteManager;

    public FavoriteEventsPresenter(Context context) {
        (GoeventApplication.getApplicationComponent(context)).inject(this);
    }

    public void load() {
        List<Event> favorites = favoriteManager.get();
        if (favorites != null && favorites.size() > 0) {
            getView().show(favorites);
        } else {
            getView().showEmpty();
        }
    }

    public void onDestroyView() {
    }

    public void onItemClick(Event event, ImageView sharedImageView, String transitionName) {
        getView().goToDetailEvent(event, sharedImageView, transitionName);
    }

    public void onUndoClick(Event event) {
        favoriteManager.add(event);
    }

    public void onItem(Event item, int position) {
        favoriteManager.remove(item);
        getView().removeItem(item, position);
    }

    public void deleteItem(Event event) {
        favoriteManager.remove(event);
    }

    public void restore(List<Event> data) {
        if (data != null && data.size() > 0) {
            getView().show(data);
        } else {
            getView().showEmpty();
        }
    }
}
