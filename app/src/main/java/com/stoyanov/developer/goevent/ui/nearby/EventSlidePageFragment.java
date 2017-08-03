package com.stoyanov.developer.goevent.ui.nearby;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.manager.NavigationManager;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.utill.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventSlidePageFragment extends Fragment {

    public static final String KEY_PARCELABLE_DATA = "KEY_PARCELABLE_DATA";
    @BindView(R.id.slide_page_date)
    TextView date;
    @BindView(R.id.slide_page_name)
    TextView name;
    @BindView(R.id.slide_page_image)
    ImageView image;
    @BindView(R.id.slide_page_progressbar)
    ProgressBar progressBar;
    private Unbinder unbinder;

    public static EventSlidePageFragment newInstance(Event event) {
        EventSlidePageFragment fragment = new EventSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARCELABLE_DATA, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_page_event, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationManager.goToDetailEvent(getActivity().getSupportFragmentManager(),
                        (Event) getArguments().getParcelable(KEY_PARCELABLE_DATA));
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Event event = getArguments().getParcelable(KEY_PARCELABLE_DATA);
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
            if (event.getStartTime() != null && event.getEndTime() != null) {
                date.setText(DateUtil.toDuration(DateUtil.toDate(event.getStartTime()),
                        DateUtil.toDate(event.getEndTime())));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

