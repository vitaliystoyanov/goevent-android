package com.stoyanov.developer.goevent.mvp.view;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;
import java.util.Set;

public interface EventsView extends BaseView {

    void showEvents(List<Event> events);

    void showEmpty();

    void clearEvents();

    void showProgress(boolean state);

    void showMessageNetworkError();

    void goToDetailEvent(Event event);

    void goToSearchEvents();

    void showCategories(Set<Category> categories);

    void showMessageAddedToFavorite();

}
