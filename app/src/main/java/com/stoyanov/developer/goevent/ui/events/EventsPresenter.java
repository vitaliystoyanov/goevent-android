package com.stoyanov.developer.goevent.ui.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.widget.ImageView;

import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Category;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.LocationPref;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;
import com.stoyanov.developer.goevent.utill.DateUtil;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
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

    public void load(LocationPref location) {
        cachedLocation = location;
        disposable.add(getEvents(location, false));
    }

    @NonNull
    private Disposable getEvents(@NonNull LocationPref location, boolean refreshCache) {
        return repository.getEventsBy(location.getLatitude(), location.getLongitude(), refreshCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(e -> getView().showProgress(true))
                .subscribe(events -> {
                    show(events, true);
                    getView().showProgress(false);
                });
    }

    private void show(List<Event> events, boolean animate) {
        if (events != null && events.size() > 0) {
            Set<Category> set = new HashSet<>();
            for (Event e : events) {
                set.add(new Category(e.getCategory()));
            }
            getView().showCategories(set);
            getView().showEvents(events, animate);
        } else {
            getView().showEmpty();
        }
    }

    public void onRefresh() {
        disposable.clear();
        disposable.add(getEvents(cachedLocation, true));
    }

    public void onItem(Event event, ImageView sharedImageView, String transitionName) {
        getView().goToDetailEvent(event, sharedImageView, transitionName);
    }

    public void onActionSearch() {
        getView().goToSearchEvents();
    }

    public void onItemStar(Event item) {
        savedEvent = item;
    }

    public void onLike() {
        if (savedEvent != null) favoriteManager.add(savedEvent);
    }

    public void onUnlike() {
        if (savedEvent != null) favoriteManager.remove(savedEvent);
    }

    public void pause() {
        disposable.clear();
    }

    public void loadForToday() {
        Calendar start = Calendar.getInstance();
        start.setFirstDayOfWeek(Calendar.MONDAY);
        start.getTime();

        Calendar end = Calendar.getInstance();
        end.setFirstDayOfWeek(Calendar.MONDAY);
        end.roll(Calendar.DAY_OF_MONTH, 1);
        end.getTime();

        Pair<String, String> dates = DateUtil.toDurationWithoutTimeRange(start.getTime(), end.getTime());
        load(dates.first, dates.second);
    }

    public void loadForTomorrow() {
        Calendar start = Calendar.getInstance();
        start.setFirstDayOfWeek(Calendar.MONDAY);
        start.roll(Calendar.DAY_OF_MONTH, 1);
        start.getTime();

        Calendar end = Calendar.getInstance();
        end.setFirstDayOfWeek(Calendar.MONDAY);
        end.roll(Calendar.DAY_OF_MONTH, 2);
        end.getTime();

        Pair<String, String> dates = DateUtil.toDurationWithoutTimeRange(start.getTime(), end.getTime());
        load(dates.first, dates.second);
    }

    public void loadForWeekend() {
        Calendar start = Calendar.getInstance();
        start.setFirstDayOfWeek(Calendar.MONDAY);
        start.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        start.getTime();

        Calendar end = Calendar.getInstance();
        end.setFirstDayOfWeek(Calendar.MONDAY);
        end.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        end.roll(Calendar.DAY_OF_MONTH, 1);
        end.getTime();

        Pair<String, String> dates = DateUtil.toDurationWithoutTimeRange(start.getTime(), end.getTime());
        load(dates.first, dates.second);
    }

    public void loadForCustomDateRange(Pair<String, String> sinceUntil) {
        if (!sinceUntil.first.equals(sinceUntil.second)) {
            load(sinceUntil.first, sinceUntil.second);
        } else {

        }
    }

    private void load(String sinceDate, String untilDate) {
        disposable.add(repository.getEventsBy(cachedLocation.getLatitude(), cachedLocation.getLongitude(),
                sinceDate, untilDate,
                false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(e -> {
                    getView().clearEvents();
                    getView().showProgress(true);
                })
                .subscribe(events -> {
                    show(events, true);
                    getView().showProgress(false);
                }, throwable -> getView().showError())
        );
    }

    public void restore(List<Event> data) {
        show(data, false);
    }
}
