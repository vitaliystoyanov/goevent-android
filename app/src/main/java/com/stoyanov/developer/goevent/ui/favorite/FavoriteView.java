package com.stoyanov.developer.goevent.ui.favorite;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.BaseView;

import java.util.List;

public interface FavoriteView extends BaseView {

    void showProgressBar(boolean state);

    void showSaved(List<Event> events);

    void showEmpty();

    void goToDetailEvent(Event event);

    void removeItem(Event event, int itemPosition);

}
