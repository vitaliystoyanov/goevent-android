package com.stoyanov.developer.goevent.mvp.presenter;

import com.stoyanov.developer.goevent.mvp.view.ContainerView;

public class ContainerPresenter extends BasePresenter<ContainerView> {
    private static final String TAG = "ContainerPresenter";

    public void onItemListOfEvents() {
        getView().goToListOfEvents();
    }

    public void onItemLogin() {
        getView().goToLogin();
    }

    public void onItemNotifications() {
        getView().goToNotificationSettings();
    }

    public void onItemFavorites() {
        getView().goToFavorites();
    }

    public void onItemNearby() {
        getView().goToNearby();
    }

    public void onStart() {
        getView().goToHome();
    }

    public void onItemDefineLocation() {
        getView().goToDefineLocation();
    }

    public void onItemHome() {
        getView().goToHome();
    }
}
