package com.fa.grubot.objects.users;

import android.os.AsyncTask;

import com.fa.grubot.App;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;

public class CurrentUser implements Serializable {

    private TLUser telegramUser;
    private VkUser vkUser;

    private User telegramChatUser = null;
    private User vkChatUser = null;

    public CurrentUser(@Nullable TLUser telegramUser, @Nullable VkUser vkUser) {
        this.telegramUser = telegramUser;
        this.vkUser = vkUser;
    }

    public TLUser getTelegramUser() {
        return telegramUser;
    }

    public VkUser getVkUser() {
        return vkUser;
    }

    public void setTelegramUser(TLUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public void setVkUser(VkUser vkUser) {
        this.vkUser = vkUser;
    }

    public boolean hasVkUser(){
        return vkUser != null;
    }

    public boolean hasTelegramUser(){
        return telegramUser != null;
    }

    public void resetVkUser(){
        vkUser = null;
    }

    public void resetTelegramUser(){
        telegramUser = null;
        AsyncTask.execute(() -> {
            try {
                App.INSTANCE.getNewTelegramClient(null).authLogOut();
            } catch (RpcErrorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            App.INSTANCE.closeTelegramClient();
        });
    }

    public User getTelegramChatUser() {
        return telegramChatUser;
    }

    public void setTelegramChatUser(User telegramChatUser) {
        this.telegramChatUser = telegramChatUser;
    }

    public User getVkChatUser() {
        return vkChatUser;
    }

    public void setVkChatUser(User vkChatUser) {
        this.vkChatUser = vkChatUser;
    }
}
