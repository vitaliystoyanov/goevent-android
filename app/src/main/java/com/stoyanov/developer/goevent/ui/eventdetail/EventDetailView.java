package com.stoyanov.developer.goevent.ui.eventdetail;

import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.mvp.view.BaseView;

public interface EventDetailView extends BaseView {

    void showImage(String url);

    void showDescription(String desc, String name);

    void showWhen(String dateFrom, String dateTo);

    void showLocation(Location location);

    void showRoutes();

    void showCategory(String category);

    void showIsFavorite(boolean b);

    void addToCalendar();

    void openGoogleMapApp(LatLng latLng);

    void showProgress(boolean b);
}
