package com.stoyanov.developer.goevent.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.stoyanov.developer.goevent.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.notification_settings);
    }
}
