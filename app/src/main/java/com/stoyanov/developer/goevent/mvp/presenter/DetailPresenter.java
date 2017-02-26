package com.stoyanov.developer.goevent.mvp.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.MainApplication;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.DetailEventView;

public class DetailPresenter extends BasePresenter<DetailEventView> {

    public void onStart(Event event) {
        getView().showImage(event.getPicture());
        getView().showDescription(event.getDescription(), event.getName());
        getView().showLocation(event.getLocation());
        getView().showWhen(event.getStartTime(), event.getEndTime());
        getView().showCategory(event.getCategory());
    }

    public void onDestroyView() {

    }

    public void onSaveClick() {
        getView().showMessageAdded();
    }

    public void onCalendarAddClick() {
        getView().addToCalendar();
    }

    public void onOpenMapClick(LatLng latLng) {
        getView().openGoogleMapApp(latLng);
    }
}
