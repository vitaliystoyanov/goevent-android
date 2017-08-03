package com.stoyanov.developer.goevent.ui.events;

import android.content.Context;
import android.support.annotation.NonNull;

import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventsPresenter extends BasePresenter<EventsView> {
    private Event savedEvent;
    private LocationPref cachedLocation;
    private CompositeDisposable disposable;
    private EventsRepository repository;
    private FavoriteManager favoriteManager;

    public EventsPresenter(Context context, FavoriteManager manager, EventsRepository repository) {
        disposable = new CompositeDisposable();
        this.favoriteManager = manager;
        this.repository = repository;
    }

    public void provideData(LocationPref location) {
        cachedLocation = location;
        disposable.add(getEvents(location, false));
    }

    @NonNull
    private Disposable getEvents(@NonNull LocationPref location, boolean refreshCache) {
        return repository.getEventsByLocation(location.getLatitude(), location.getLongitude(), refreshCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(e -> getView().showProgress(true))
                .subscribe(events -> {
                    if (events != null && events.size() > 0) {
                        getView().showEvents(events);
                        Set<Category> set = new HashSet<>();
                        for (Event e : events) {
                            set.add(new Category(e.getCategory()));
                        }
                        getView().showCategories(set);
                    } else {
                        getView().showEmpty();
                    }
                    getView().showProgress(false);
                });
    }

    public void onRefresh() {
        disposable.clear();
        disposable.add(getEvents(cachedLocation, true));
    }

    public void onItem(Event event) {
        getView().goToDetailEvent(event);
    }

    public void onActionSearch() {
        getView().goToSearchEvents();
    }

    public void onItemStar(Event item) {
        savedEvent = item;
    }

    public void onLike() {
        if (savedEvent != null) {
            favoriteManager.add(savedEvent);
        }
    }

    public void onUnlike() {
        if (savedEvent != null) favoriteManager.remove(savedEvent);
    }

    public void pause() {
        disposable.clear();
    }
}
