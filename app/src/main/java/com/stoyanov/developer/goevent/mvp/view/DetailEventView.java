package com.stoyanov.developer.goevent.mvp.view;

import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;

public interface DetailEventView extends BaseView {

    void showImage(String url);

    void showDescription(String desc, String name);

    void showWhen(String dateFrom, String dateTo);

    void showLocation(Location location);

    void showRoutes();

    void showCategory(String category);

    void showMessageAdded();

    void addToCalendar();

    void openGoogleMapApp(LatLng latLng);

}
