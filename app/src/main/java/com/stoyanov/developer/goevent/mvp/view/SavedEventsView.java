package com.stoyanov.developer.goevent.mvp.view;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface SavedEventsView extends BaseView {

    void showProgressBar(boolean state);

    void showSaved(List<Event> events);

    void showEmpty();

    void goToDetailEvent(Event event);

    void removeItemFromList(Event event, int itemPosition);

}
