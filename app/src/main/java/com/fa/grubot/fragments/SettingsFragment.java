package com.fa.grubot.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.R;
import com.fa.grubot.util.Globals;

import java.io.Serializable;

public class SettingsFragment extends PreferenceFragment implements Serializable {

    private transient Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setupToolbar();
        setupViews();
        return super.onCreateView(inflater, container, savedInstanceState);
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
            return true;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("mytag", "onDestroy");
        toolbar.setVisibility(View.GONE);
    }
}
