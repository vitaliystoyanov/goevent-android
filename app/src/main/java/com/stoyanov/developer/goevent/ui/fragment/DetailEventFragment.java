package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.view.DetailEventView;

public class DetailEventFragment extends Fragment implements DetailEventView {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_event, null);
    }
}
