package com.stoyanov.developer.goevent.ui.main;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.BaseView;

import java.util.List;
import java.util.Set;

public interface MainView extends BaseView {

    void showCategories(Set<Category> categories);

    void showPopularEvents(List<Event> data);

    void showEmpty();

    void showProgress(boolean isLoading);

    void showError();
}
