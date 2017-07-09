package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.ui.activity.ContainerActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment {
    private Unbinder unbinder;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(getActivity(),
                ((ContainerActivity) getActivity()).getDrawerLayout(),
                toolbar, R.string.drawer_open, R.string.drawer_close);
        ((ContainerActivity) getActivity()).setDrawerLayoutListener(drawerToggle);
        toolbar.setTitle(R.string.title_home);
    }

    @Override
    public void onStart() {
        super.onStart();
        drawerToggle.syncState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
