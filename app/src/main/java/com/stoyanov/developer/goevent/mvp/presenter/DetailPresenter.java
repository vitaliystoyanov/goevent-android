package com.stoyanov.developer.goevent.mvp.presenter;

import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.DetailEventView;

public class DetailPresenter extends BasePresenter<DetailEventView> {


    public void onStart(Event event) {
        getView().showImage(event.getPicture());
        getView().showDescription(event.getDescription(), event.getName());
        getView().showLocation(event.getLocation());
        getView().showWhen(event.getStartTime(), event.getEndTime());
    }

    public void onDestroyView() {

    }

    public void onLikeClick() {
        getView().showMessageAddedToFavorites();
    }
}
