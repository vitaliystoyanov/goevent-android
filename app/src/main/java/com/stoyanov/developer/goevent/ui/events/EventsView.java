package com.stoyanov.developer.goevent.ui.events;

import android.widget.ImageView;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.BaseView;

import java.util.List;
import java.util.Set;

public interface EventsView extends BaseView {

    void showEvents(List<Event> events);

    void showEmpty();

    void clearEvents();

    void showProgress(boolean state);

    void goToDetailEvent(Event event, ImageView sharedImageView, String transitionName);

    void goToSearchEvents();

    void showCategories(Set<Category> categories);

    void showAddedToSaved();

    void showError();
}
