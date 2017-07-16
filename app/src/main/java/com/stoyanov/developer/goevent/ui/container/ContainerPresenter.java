package com.stoyanov.developer.goevent.ui.container;

import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

public class ContainerPresenter extends BasePresenter<ContainerView> {

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

    public void openHome() {
        getView().goToHome();
    }

    public void onItemDefineLocation() {
        getView().goToDefineLocation();
    }

    public void onItemHome() {
        getView().goToHome();
    }

}
