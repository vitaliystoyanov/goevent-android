package com.stoyanov.developer.goevent.mvp.model.repository;

import android.content.Context;
import android.support.v4.content.Loader;

import com.stoyanov.developer.goevent.mvp.model.domain.Events;

import java.util.List;

public class EventsLoader extends Loader<List<Events>> {

    public EventsLoader(Context context) {
        super(context);
    }


}
