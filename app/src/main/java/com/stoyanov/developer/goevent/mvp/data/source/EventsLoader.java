package com.stoyanov.developer.goevent.mvp.data.source;

import android.content.Context;
import android.support.v4.content.Loader;

import com.stoyanov.developer.goevent.mvp.data.Event;

import java.util.List;

public class EventsLoader extends Loader<List<Event>> {

    public EventsLoader(Context context) {
        super(context);
    }


}
