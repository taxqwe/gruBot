package com.fa.grubot.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.util.Globals;

public class SettingsFragment extends PreferenceFragment {

    private Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setupToolbar();
        setupViews();
    }

    private void setupToolbar() {
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Настройки");
    }

    private void setupViews() {
        SwitchPreference animationsSwitch = (SwitchPreference) findPreference("animationsSwitch");
        SwitchPreference backstackSwitch = (SwitchPreference) findPreference("backstackSwitch");

        animationsSwitch.setOnPreferenceChangeListener((preference, o) -> {
            Globals.Variables.areAnimationsEnabled = (boolean) o;
            return true;
        });

        backstackSwitch.setOnPreferenceChangeListener((preference, o) -> {
            Globals.Variables.isBackstackEnabled = (boolean) o;
            Log.e("mytag", String.valueOf(Globals.Variables.isBackstackEnabled));
            return true;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setVisibility(View.GONE);
    }
}
