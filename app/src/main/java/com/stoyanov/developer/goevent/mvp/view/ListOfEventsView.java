package com.stoyanov.developer.goevent.mvp.view;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface ListOfEventsView extends BaseView {

    void showEvents(List<Event> events);

    void showEmpty();

    void showProgressBar(boolean state);

    void showMessageNetwotkError();

    void goToDetailEvent(Event event);

    void goToSearchEvents();

    void showMessageAddedToFavorite();

}
