package com.stoyanov.developer.goevent.ui.container;

import com.stoyanov.developer.goevent.mvp.view.BaseView;

public interface ContainerView extends BaseView {

    void goToListOfEvents();

    void goToMain();

    void goToLogin();

    void goToFavorites();

    void goToNearby();

    void goToNotificationSettings();

    void goToDefineLocation();

}
