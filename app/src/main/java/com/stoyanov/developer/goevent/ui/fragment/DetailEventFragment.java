package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.view.DetailEventView;

public class DetailEventFragment extends Fragment implements DetailEventView {

    public static final String EXTRA_PARCELABLE_EVENT = "EXTRA_PARCELABLE_EVENT";

    public static Fragment newInstance(Event event) {
        Bundle bundle = new Bundle();
        Fragment fragment = new DetailEventFragment();
        bundle.putParcelable(EXTRA_PARCELABLE_EVENT, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_event, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Event event = (Event) getArguments().getParcelable(EXTRA_PARCELABLE_EVENT);
        if (event.getPicture() != null) {
            Picasso.with(getContext())
                    .load(event.getPicture())
                    .into((ImageView) getView().findViewById(R.id.detail_event_image));
        }
        Toast.makeText(getContext(), "Event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }
}
