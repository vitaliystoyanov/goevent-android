package com.stoyanov.developer.goevent.mvp.view;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public interface MainView extends BaseView {

    void showListOfEvents(List<Event> events);

}
