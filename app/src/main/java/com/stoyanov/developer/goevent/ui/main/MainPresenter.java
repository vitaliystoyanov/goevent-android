package com.stoyanov.developer.goevent.ui.main;

import android.content.Context;

import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> {
    private EventsRepository repository;
    private LocationPref location;
    private Context context;
    private Disposable disposable;

    public MainPresenter(Context context, EventsRepository repository) {
        this.context = context;
        this.repository = repository;
    }

    public void provideData(LocationPref pref) {
        location = pref;
        if (pref != null) {
            disposable = repository.getEventsByLocation(pref.getLatitude(), pref.getLongitude(), true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(e -> getView().showProgress(true))
                    .subscribe(events -> {
                        if (events != null && events.size() > 0) {
                            getView().showPopularEvents(events);
                            Set<Category> set = new HashSet<>();
                            for (Event e : events) {
                                set.add(new Category(e.getCategory()));
                            }
                            getView().showCategories(set);
                        } else {
                            getView().showEmpty();
                        }
                        getView().showProgress(false);
                    }, throwable -> getView().showError());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}
