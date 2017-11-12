package com.stoyanov.developer.goevent.ui.nearby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
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

public class EventSlidePageFragment extends Fragment {

    public static final String KEY_PARCELABLE_DATA = "KEY_PARCELABLE_DATA";
    @BindView(R.id.slide_page_name)
    TextView name;
    @BindView(R.id.slide_page_image)
    ImageView image;
    @BindView(R.id.slide_page_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    private Unbinder unbinder;
    private Event event;
    @Inject
    NavigationManager navigationManager;

    public static EventSlidePageFragment newInstance(Event event) {
        EventSlidePageFragment fragment = new EventSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARCELABLE_DATA, Parcels.wrap(event));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder()
                .activityComponent(((ContainerActivity) getActivity()).getActivityComponent())
                .build()
                .inject(this);
        event = Parcels.unwrap(getArguments().getParcelable(KEY_PARCELABLE_DATA));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_page_event, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        rootView.setOnClickListener(view ->
                navigationManager.goToDetailEvent(event));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (event != null) {
            ViewCompat.setTransitionName(image, event.getName());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (event != null) {
            if (event.getPicture() != null) {
                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(getContext())
                        .load(event.getPicture())
                        .into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
            name.setText(event.getName());
            tvDay.setText(DateUtil.toDay(event.getStartTime()));
            tvMonth.setText(DateUtil.toMonth(event.getStartTime()));
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}

