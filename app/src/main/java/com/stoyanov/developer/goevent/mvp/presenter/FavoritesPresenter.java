package com.stoyanov.developer.goevent.mvp.presenter;

import com.stoyanov.developer.goevent.mvp.view.FavoritesView;

public class FavoritesPresenter extends BasePresenter<FavoritesView> {


    public void onStart() {
        getView().showProgressBar(true);
    }

    public void onDestroyView() {

    }
}
