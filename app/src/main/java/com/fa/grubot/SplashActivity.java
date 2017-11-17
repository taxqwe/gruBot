package com.fa.grubot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadPreferences();
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        SplashActivity.this.finish();
    }

    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        App.INSTANCE.setAnimationsEnabled(prefs.getBoolean("animationsSwitch", false));
        App.INSTANCE.setBackstackEnabled(prefs.getBoolean("backstackSwitch", false));
        App.INSTANCE.setSlidrEnabled(prefs.getBoolean("slidrSwitch", true));
    }
}
