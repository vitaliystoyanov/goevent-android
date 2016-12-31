package com.stoyanov.developer.goevent.mvp.presenter;

import com.stoyanov.developer.goevent.mvp.view.MainView;

public class MainPresenter extends BasePresenter<MainView> {
    private static final String TAG = "MainPresenter";

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
        getView().goToListOfEvents();
    }

    public void onItemDefineLocation() {
        getView().goToDefineLocation();
    }
}
