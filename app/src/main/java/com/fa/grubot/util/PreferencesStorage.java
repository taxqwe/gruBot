package com.fa.grubot.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ni.petrov on 05/11/2017.
 */

public class PreferencesStorage {

    private final String NAME = "ApplicationPreferences";

    private Context ctx;

    SharedPreferences preferences;

    public PreferencesStorage(Context ctx) {
        this.ctx = ctx;

        preferences = ctx.getSharedPreferences(NAME, ctx.MODE_PRIVATE);
    }

    public boolean getBoolean(String name, boolean def) {
        return preferences.getBoolean(name, def);
    }

    public void putBoolean(String name, boolean value) {
        preferences.edit().putBoolean(name, value).apply();
    }
}
