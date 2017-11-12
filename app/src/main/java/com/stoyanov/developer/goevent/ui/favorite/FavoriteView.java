package com.stoyanov.developer.goevent.ui.favorite;

import android.widget.ImageView;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.BaseView;

import java.util.List;

public interface FavoriteView extends BaseView {

    void showProgressBar(boolean state);

    void show(List<Event> events);

    void showEmpty();

    void goToDetailEvent(Event event, ImageView sharedImageView, String transitionName);

    void removeItem(Event event, int itemPosition);

}
