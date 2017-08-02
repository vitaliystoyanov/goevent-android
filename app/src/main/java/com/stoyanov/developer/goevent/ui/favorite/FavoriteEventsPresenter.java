package com.stoyanov.developer.goevent.ui.favorite;

import android.content.Context;

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

    public void onStart() {
//        getView().visibleProgress(true);
        List<Event> savedEvents = favoriteManager.get();
        if (savedEvents != null && savedEvents.size() > 0) {
            getView().showSaved(savedEvents);
        } else {
            getView().showEmpty();
        }
    }

    public void onDestroyView() {
    }

    public void onItemClick(Event event) {
        getView().goToDetailEvent(event);
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
}
