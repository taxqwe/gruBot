package com.fa.grubot.util;

import android.content.Context;

import com.fa.grubot.App;
import com.github.badoualy.telegram.api.TelegramApiStorage;
import com.github.badoualy.telegram.mtproto.auth.AuthKey;
import com.github.badoualy.telegram.mtproto.model.DataCenter;
import com.github.badoualy.telegram.mtproto.model.MTSession;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TmApiStorage implements TelegramApiStorage {

    private File authKeyFile;
    private File nearestDcFile;

    public TmApiStorage(File authKeyFile, File nearestDcFile) {
        super();
        this.authKeyFile = authKeyFile;
        this.nearestDcFile = nearestDcFile;
    }

    @Override
    public void saveAuthKey(@NotNull AuthKey authKey) {
        try {
            FileUtils.writeByteArrayToFile(authKeyFile, authKey.getKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public AuthKey loadAuthKey() {
        try {
            return new AuthKey(FileUtils.readFileToByteArray(authKeyFile));
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException))
                e.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveDc(@NotNull DataCenter dataCenter) {
        try {
            FileUtils.write(nearestDcFile, dataCenter.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public DataCenter loadDc() {
        try {
            String[] infos = FileUtils.readFileToString(nearestDcFile, "UTF-8").split(":");
            return new DataCenter(infos[0], Integer.parseInt(infos[1]));
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException))
                e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteAuthKey() {
        try {
            FileUtils.forceDelete(authKeyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDc() {
        try {
            FileUtils.forceDelete(nearestDcFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveSession(@Nullable MTSession session) {

    }

    @Nullable
    @Override
    public MTSession loadSession() {
        return null;
    }
}
