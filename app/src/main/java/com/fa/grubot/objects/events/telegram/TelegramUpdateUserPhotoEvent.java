package com.fa.grubot.objects.events.telegram;

import com.fa.grubot.objects.misc.TelegramPhoto;

public class TelegramUpdateUserPhotoEvent {
    private int userId;
    private String imgUri;

    public TelegramUpdateUserPhotoEvent(int userId, String imgUri) {
        this.userId = userId;
        this.imgUri = imgUri;
    }

    public int getUserId() {
        return userId;
    }

    public String getImgUri() {
        return imgUri;
    }
}
