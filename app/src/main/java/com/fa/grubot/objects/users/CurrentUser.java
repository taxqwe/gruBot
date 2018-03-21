package com.fa.grubot.objects.users;

import com.github.badoualy.telegram.tl.api.TLUser;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class CurrentUser implements Serializable {

    private TLUser telegramUser;
    private VkUser vkUser;

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
}
