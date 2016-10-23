package com.stoyanov.developer.goevent.mvp.model.repository.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orm.SugarRecord;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import java.util.List;

public class EventsLocalStorageImp implements EventsStorage {

    @Nullable
    @Override
    public List<Event> getEvents() {
        return SugarRecord.listAll(Event.class);
    }

    @Override
    public void saveEvents(@NonNull List<Event> events) {
        SugarRecord.deleteAll(Event.class);
        for (Event event : events) {
            SugarRecord.save(event);
        }
    }
}
