package com.stoyanov.developer.goevent.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventSliderFragment extends Fragment {
    public static final String KEY_ARG_EVENT = "key-arg-event";
    private Unbinder unbinder;

    @BindView(R.id.img_event_slider)
    ImageView image;

    public static EventSliderFragment newInstance(@NonNull Event event) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ARG_EVENT, Parcels.wrap(event));
        EventSliderFragment fragment = new EventSliderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_slider, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Event e = Parcels.unwrap(getArguments().getParcelable(KEY_ARG_EVENT));
        if (e != null && e.getPicture() != null) {
            Picasso.with(getContext())
                    .load(e.getPicture())
                    .into(image);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
