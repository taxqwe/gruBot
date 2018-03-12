package com.fa.grubot.objects.group;

import com.github.badoualy.telegram.tl.api.TLUser;

import java.io.Serializable;

public class CurrentUser implements Serializable {

    private TLUser telegramUser;
    private Object vkUser;

    public CurrentUser(TLUser telegramUser, Object vkUser) {
        this.telegramUser = telegramUser;
        this.vkUser = vkUser;
    }

    public TLUser getTelegramUser() {
        return telegramUser;
    }

    public Object getVkUser() {
        return vkUser;
    }

    public void setTelegramUser(TLUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public void setVkUser(Object vkUser) {
        this.vkUser = vkUser;
    }
}
