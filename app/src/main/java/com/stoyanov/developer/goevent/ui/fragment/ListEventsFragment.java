package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanov.developer.goevent.R;

public class ListEventsFragment extends Fragment {

    public static ListEventsFragment newInstance() {
        ListEventsFragment fragment = new ListEventsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_events, null);
    }
}
