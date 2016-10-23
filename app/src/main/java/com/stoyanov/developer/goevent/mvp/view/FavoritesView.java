package com.stoyanov.developer.goevent.mvp.view;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface FavoritesView extends BaseView {

    void showProgressBar(boolean state);

    void showEvents(List<Event> events);
}
