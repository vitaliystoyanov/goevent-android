package com.stoyanov.developer.goevent.ui.eventdetail;

import com.google.android.gms.maps.model.LatLng;
import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.repository.EventsRepository;
import com.stoyanov.developer.goevent.mvp.presenter.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;

public class EventDetailPresenter extends BasePresenter<EventDetailView> {
    private CompositeDisposable disposable;
    private EventsRepository repository;
    private FavoriteManager favoriteManager;
    private Event e;

    public EventDetailPresenter(EventsRepository repository, FavoriteManager favoriteManager) {
        disposable = new CompositeDisposable();
        this.repository = repository;
        this.favoriteManager = favoriteManager;
    }

    public void onStart(Event e) {
        disposable.clear();
        this.e = e;
       /* disposable.add(repository.getEvent(e.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(event -> getView().showProgress(true))
                .subscribe(event -> {
                    getView().showImage(event.getPicture());
                    getView().showDescription(event.getDescription(), event.getName());
                    getView().showLocation(event.getLocation());
                    getView().showWhen(event.getStartTime(), event.getEndTime());
                    getView().showCategory(event.getCategory());
                    getView().showProgress(false);
                }));*/
        getView().showImage(e.getPicture());
        getView().showDescription(e.getDescription(), e.getName());
        getView().showLocation(e.getLocation());
        getView().showWhen(e.getStartTime(), e.getEndTime());
        getView().showCategory(e.getCategory());
        getView().showProgress(false);

        getView().showIsFavorite(favoriteManager.isSaved(e) ? true : false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        disposable.dispose();
    }

    public void onStarClick() {
        if (!favoriteManager.isSaved(e)) {
            favoriteManager.add(e);
            getView().showIsFavorite(true);
        } else {
            favoriteManager.remove(e);
            getView().showIsFavorite(false);
        }
    }

    public void onCalendarAddClick() {
        getView().addToCalendar();
    }

    public void onOpenMapClick(LatLng latLng) {
        getView().openGoogleMapApp(latLng);
    }
}
