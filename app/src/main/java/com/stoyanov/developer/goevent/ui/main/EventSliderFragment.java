package com.stoyanov.developer.goevent.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.di.component.DaggerFragmentComponent;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.ui.container.ContainerActivity;
import com.stoyanov.developer.goevent.utill.DateUtil;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventSliderFragment extends Fragment {
    public static final String KEY_ARG_EVENT = "key-arg-event";
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_circle)
    ImageView ivCircle;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.fl_overlay)
    FrameLayout flOverlay;
    private Unbinder unbinder;

    @BindView(R.id.img_event_slider)
    ImageView image;
    @Inject
    NavigationManager navigationManager;
    Event event;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((ContainerActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
    }

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        event = Parcels.unwrap(getArguments().getParcelable(KEY_ARG_EVENT));
        if (event != null && event.getPicture() != null) {
            ViewCompat.setTransitionName(image, event.getName());
        }
        flOverlay.setOnClickListener(v -> navigationManager.goToDetailEvent(event, image, ViewCompat.getTransitionName(image)));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (event != null && event.getPicture() != null) {
            Picasso.with(getContext())
                    .load(event.getPicture())
                    .into(image);
            tvName.setText(event.getName());
            tvDay.setText(DateUtil.toDay(event.getStartTime()));
            tvMonth.setText(DateUtil.toMonth(event.getStartTime()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
