package com.stoyanov.developer.goevent.mvp.presenter;

import com.stoyanov.developer.goevent.mvp.view.BaseView;

public abstract class BasePresenter<T extends BaseView> {

    private T view;

    public T getView() {
        return view;
    }

    public void attach(T view) {
        this.view = view;
    }

    public void detach() {
        onDetach();
        view = null;
    }

    public void onDetach() {

    }
}
