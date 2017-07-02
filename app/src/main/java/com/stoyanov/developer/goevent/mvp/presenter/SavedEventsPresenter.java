package com.stoyanov.developer.goevent.mvp.presenter;

import android.content.Context;

import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.SavedEventsManager;
import com.stoyanov.developer.goevent.mvp.view.SavedEventsView;

import java.util.List;

import javax.inject.Inject;

public class SavedEventsPresenter extends BasePresenter<SavedEventsView> {
    @Inject
    SavedEventsManager savedEventsManager;

    public SavedEventsPresenter(Context context) {
        (GoeventApplication.getApplicationComponent(context)).inject(this);
    }

    public void onStart() {
//        getView().visibleProgress(true);
        List<Event> savedEvents = savedEventsManager.get();
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
        savedEventsManager.add(event);
    }

    public void onItem(Event item, int position) {
        savedEventsManager.remove(item);
        getView().removeItemFromList(item, position);
    }

    public void onUnlike() {
        // FIXME: 22.11.2016
    }
}
