package com.fa.grubot;

import android.app.Application;
import android.graphics.Color;

import com.fa.grubot.objects.group.CurrentUser;
import com.fa.grubot.util.TmApiStorage;
import com.github.badoualy.telegram.api.Kotlogram;
import com.github.badoualy.telegram.api.TelegramApp;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.api.UpdateCallback;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.io.File;

public class App extends Application {
    public static App INSTANCE;

    private boolean isBackstackEnabled = false;
    private boolean areAnimationsEnabled = false;
    private boolean isSlidrEnabled = true;

    private TelegramClient telegramClient;
    private CurrentUser currentUser;

    private static final int API_ID = ;
    private static final String API_HASH = "";

    private static final String APP_VERSION = "1.0";
    private static final String MODEL = "Dev";
    private static final String SYSTEM_VERSION = "Dev";
    private static final String LANG_CODE = "en";

    private File authKeyFile;
    private File nearestDcFile;

    private TelegramApp application;

    @Override
    public void onCreate() {
        super.onCreate();

        authKeyFile = new File(this.getApplicationContext().getFilesDir(), "auth.key");
        nearestDcFile = new File(this.getApplicationContext().getFilesDir(), "dc.save");

        application = new TelegramApp(API_ID, API_HASH, MODEL, SYSTEM_VERSION, APP_VERSION, LANG_CODE);
        INSTANCE = this;
    }

    public TelegramClient getNewTelegramClient(UpdateCallback callback) {
        if (telegramClient != null && !telegramClient.isClosed())
            closeTelegramClient();

        TmApiStorage apiStorage = new TmApiStorage(authKeyFile, nearestDcFile);
        telegramClient = Kotlogram.getDefaultClient(application, apiStorage, apiStorage.loadDc(), callback);
        return telegramClient;
    }

    public void closeTelegramClient() {
        if (telegramClient != null)
            telegramClient.close(false);
    }

    public void setCurrentUser(CurrentUser user) {
        this.currentUser = user;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public boolean isBackstackEnabled() {
        return isBackstackEnabled;
    }

    public boolean areAnimationsEnabled() {
        return areAnimationsEnabled;
    }

    public boolean isSlidrEnabled() {
        return isSlidrEnabled;
    }

    public void setBackstackEnabled(boolean backstackEnabled) {
        isBackstackEnabled = backstackEnabled;
    }

    public void setAnimationsEnabled(boolean areAnimationsEnabled) {
        this.areAnimationsEnabled = areAnimationsEnabled;
    }

    public void setSlidrEnabled(boolean slidrEnabled) {
        isSlidrEnabled = slidrEnabled;
    }

    public SlidrConfig getSlidrConfig() {
        return new SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .sensitivity(0.7f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.5f)
                .edge(false)
                .build();
    }
}
