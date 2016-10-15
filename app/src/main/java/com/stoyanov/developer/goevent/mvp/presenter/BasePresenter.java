package com.stoyanov.developer.goevent.mvp.presenter;

import com.stoyanov.developer.goevent.mvp.view.BaseView;

public abstract class BasePresenter<T extends BaseView> {

    private T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    public T getView() {
        return view;
    }

    public abstract void onResume();

    public void onDestroy() {
        view = null;
    }
}
