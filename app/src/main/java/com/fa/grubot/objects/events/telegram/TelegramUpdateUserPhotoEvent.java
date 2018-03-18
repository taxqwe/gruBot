package com.fa.grubot.objects.events.telegram;

import com.fa.grubot.objects.misc.TelegramPhoto;

public class TelegramUpdateUserPhotoEvent {
    private int userId;
    private TelegramPhoto telegramPhoto;

    public TelegramUpdateUserPhotoEvent(int userId, TelegramPhoto telegramPhoto) {
        this.userId = userId;
        this.telegramPhoto = telegramPhoto;
    }

    public int getUserId() {
        return userId;
    }

    public TelegramPhoto getTelegramPhoto() {
        return telegramPhoto;
    }
}
