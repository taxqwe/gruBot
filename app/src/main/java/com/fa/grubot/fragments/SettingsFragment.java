package com.fa.grubot.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.App;
import com.fa.grubot.LoginActivity;
import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.objects.group.CurrentUser;
import com.fa.grubot.objects.group.VkUser;
import com.fa.grubot.util.DataType;
import com.github.badoualy.telegram.tl.api.TLUser;

import java.io.Serializable;

public class SettingsFragment extends PreferenceFragmentCompat implements Serializable {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        setupToolbar();
        setupViews();
        return view;
    }

    @Override
    public void onPause() {
        App.INSTANCE.closeTelegramClient();
        super.onPause();
    }

    @Override
    public void onStop() {
        App.INSTANCE.closeTelegramClient();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        App.INSTANCE.closeTelegramClient();
        super.onDestroyView();
    }

    private void setupToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Настройки");

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void setupViews() {
        Preference vkAccount = findPreference("vkAccount");
        Preference telegramAccount = findPreference("telegramAccount");

        SwitchPreferenceCompat animationsSwitch = (SwitchPreferenceCompat) findPreference("animationsSwitch");
        SwitchPreferenceCompat backstackSwitch = (SwitchPreferenceCompat) findPreference("backstackSwitch");
        SwitchPreferenceCompat slidrSwitch = (SwitchPreferenceCompat) findPreference("slidrSwitch");

        CurrentUser currentUser = App.INSTANCE.getCurrentUser();
        if (currentUser.getVkUser() != null) {
            VkUser vkUser = currentUser.getVkUser();
            vkAccount.setSummary(vkUser.getFirstName() + " " + vkUser.getLastName());

            //TODO logout
        }

        if (currentUser.getVkUser() == null) {
            //TODO show login form
        }

        if (currentUser.getTelegramUser() != null) {
            TLUser telegramUser = currentUser.getTelegramUser();
            telegramAccount.setSummary(telegramUser.getFirstName() + " " + telegramUser.getLastName());

            telegramAccount.setOnPreferenceClickListener(preference -> {
                    new MaterialDialog.Builder(getActivity())
                            .title("Выйти из аккаунта")
                            .content("Вы уверены, что хотите выйти из аккаунта?")
                            .positiveText(android.R.string.yes)
                            .negativeText(android.R.string.cancel)
                            .onPositive((dialog, which) -> {
                                AsyncTask.execute(() -> {
                                    try {
                                        App.INSTANCE.getNewTelegramClient(null).authLogOut();
                                        App.INSTANCE.closeTelegramClient();
                                        if (currentUser.getTelegramUser() == null && currentUser.getVkUser() == null) {
                                            startActivity(new Intent(getActivity(), LoginActivity.class));
                                            getActivity().finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            })
                            .show();

                return false;
            });
        }

        if (currentUser.getTelegramUser() == null) {
            telegramAccount.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("directLogin", DataType.Telegram);
                getActivity().startActivity(intent);
                return false;
            });
        }

        animationsSwitch.setOnPreferenceChangeListener((preference, o) -> {
            App.INSTANCE.setAnimationsEnabled((boolean) o);
            return true;
        });

        backstackSwitch.setOnPreferenceChangeListener((preference, o) -> {
            App.INSTANCE.setBackstackEnabled((boolean) o);
            return true;
        });

        slidrSwitch.setOnPreferenceChangeListener((preference, o) -> {
            App.INSTANCE.setSlidrEnabled((boolean) o);
            return true;
        });
    }
}
